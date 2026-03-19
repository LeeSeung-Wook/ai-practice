package com.example.demo.user;

import lombok.Data;

/**
 * RULE: Max, Min, Detail DTO이름을 최소, 최대, 상세를 기본으로 한다.
 */
public class UserResponse {
    @Data
    public static class SessionUser {
        private Integer id;
        private String username;

        public SessionUser(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
        }
    }

    @Data
    public static class Detail {
        private Integer id;
        private String username;
        private String email;
        private String postcode;
        private String address;
        private String detailAddress;
        private String extraAddress;

        public Detail(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail() != null ? user.getEmail() : "";
            this.postcode = user.getPostcode() != null ? user.getPostcode() : "";
            this.address = user.getAddress() != null ? user.getAddress() : "";
            this.detailAddress = user.getDetailAddress() != null ? user.getDetailAddress() : "";
            this.extraAddress = user.getExtraAddress() != null ? user.getExtraAddress() : "";
        }
    }

    @Data
    public static class Min {
        private String username;
        private String email;

        public Min(User user) {
            this.username = user.getUsername();
            this.email = user.getEmail();
        }
    }
}
