package com.rau.jp.dto;

public class ProviderResponseDto {
     private String provider;
     private boolean isValid;

     public ProviderResponseDto(String provider, boolean isValid) {
          this.provider = provider;
          this.isValid = isValid;
     }

     public String getProvider() {
          return provider;
     }

     public void setProvider(String provider) {
          this.provider = provider;
     }

     public boolean isValid() {
          return isValid;
     }

     public void setValid(boolean valid) {
          isValid = valid;
     }

     @Override
     public String toString() {
          return "ProviderResponseDto{" +
                  "provider='" + provider + '\'' +
                  ", isValid=" + isValid +
                  '}';
     }
}
