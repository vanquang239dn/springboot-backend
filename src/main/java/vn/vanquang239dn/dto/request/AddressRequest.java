package vn.vanquang239dn.dto.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddressRequest implements Serializable {

        private final String apartmentNumber;

        private final String floor;

        private final String building;

        private final String streetNumber;

        private final String street;

        private final String city;

        private final String country;

        private final String addressType;

}
