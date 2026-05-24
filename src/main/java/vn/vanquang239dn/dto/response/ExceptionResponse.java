package vn.vanquang239dn.dto.response;

import java.io.Serializable;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ExceptionResponse implements Serializable {

        private Instant timestamp;

        private int status;

        private String path;

        private String message;

        private Object details;

}