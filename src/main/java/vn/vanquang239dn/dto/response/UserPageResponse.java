package vn.vanquang239dn.dto.response;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserPageResponse implements Serializable {

        private int pageNumber;

        private int pageSize;

        private long totalElements;

        private long totalPages;

        private List<UserResponse> listUserResponse;

}
