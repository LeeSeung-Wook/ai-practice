package com.example.demo.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo._core.handler.ex.Exception400;
import com.example.demo._core.handler.ex.Exception401;
import com.example.demo.board.BoardRepository;
import com.example.demo.reply.ReplyRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    private final PasswordEncoder passwordEncoder;

    // 유저 정보 조회 (마이페이지용)
    public UserResponse.Detail findById(Integer id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new Exception400("유저 정보를 찾을 수 없습니다."));
        return new UserResponse.Detail(user);
    }

    // 회원 탈퇴 (댓글 -> 게시글 -> 유저 순으로 삭제)
    @Transactional
    public void withdraw(Integer id) {
        // 1. 내가 쓴 댓글 삭제
        replyRepository.deleteByUserId(id);
        // 2. 내 게시글에 달린 모든 댓글 삭제 (타인이 쓴 댓글 포함)
        replyRepository.deleteByBoardUserId(id);
        // 3. 내 게시글 삭제
        boardRepository.deleteByUserId(id);
        // 4. 유저 삭제
        userRepository.deleteById(id);
    }

    // 로그인 (비밀번호 비교 후 유저 정보 반환)
    public UserResponse.SessionUser login(UserRequest.Login reqDTO) {
        // 아이디로 유저 찾기
        var userOp = userRepository.findByUsername(reqDTO.getUsername());
        if (userOp.isEmpty()) {
            throw new Exception401("유저네임을 찾을 수 없습니다.");
        }

        // 비밀번호 확인 (BCrypt)
        var user = userOp.get();
        if (!passwordEncoder.matches(reqDTO.getPassword(), user.getPassword())) {
            throw new Exception401("비밀번호가 일치하지 않습니다.");
        }

        return new UserResponse.SessionUser(user);
    }

    // 회원가입 (비밀번호 암호화 후 저장)
    @Transactional
    public void join(UserRequest.Join reqDTO) {
        // 아이디 중복 체크
        var userOp = userRepository.findByUsername(reqDTO.getUsername());
        if (userOp.isPresent()) {
            throw new Exception400("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 암호화 (BCrypt)
        var encPassword = passwordEncoder.encode(reqDTO.getPassword());
        reqDTO.setPassword(encPassword);

        // 엔티티 변환 후 저장
        userRepository.save(reqDTO.toEntity());
    }

    // 아이디 중복 체크 (true: 사용 가능, false: 중복)
    public boolean usernameSameCheck(String username) {
        var userOp = userRepository.findByUsername(username);
        return userOp.isEmpty();
    }

    // 회원 정보 수정
    @Transactional
    public UserResponse.SessionUser update(Integer id, UserRequest.Update reqDTO) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new Exception400("유저 정보를 찾을 수 없습니다."));

        // 1. 비밀번호 수정 (입력된 경우에만)
        if (reqDTO.getPassword() != null && !reqDTO.getPassword().isBlank()) {
            var encPassword = passwordEncoder.encode(reqDTO.getPassword());
            user.setPassword(encPassword);
        }

        // 2. 나머지 정보 수정 (Dirty Checking)
        user.setEmail(reqDTO.getEmail());
        user.setPostcode(reqDTO.getPostcode());
        user.setAddress(reqDTO.getAddress());
        user.setDetailAddress(reqDTO.getDetailAddress());
        user.setExtraAddress(reqDTO.getExtraAddress());

        return new UserResponse.SessionUser(user);
    }
}
