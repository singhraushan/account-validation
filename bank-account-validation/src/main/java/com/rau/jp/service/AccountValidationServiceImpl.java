package com.rau.jp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rau.jp.config.ProviderProperties;
import com.rau.jp.dto.ProviderResponseDto;
import com.rau.jp.dto.RequestDto;
import com.rau.jp.dto.ResponseDto;
import com.rau.jp.exception.CustomException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Service
public class AccountValidationServiceImpl implements AccountValidationService {

    private static Logger LOG = LoggerFactory.getLogger(AccountValidationServiceImpl.class);

    /*@Autowired
    RestTemplate restTemplate;*/

    @Autowired
    ProviderProperties providerProperties;

    @Override
    public ResponseDto getProvidersResponse(RequestDto requestDto) throws CustomException {
        LOG.info("Processing requestDto: {}", requestDto);

        ProviderResponseDto[] providerResponseDto = null;

        if (requestDto.getProviders() == null || requestDto.getProviders().length == 0) {
            providerResponseDto = onEmptyProviders(requestDto.getAccountNumber());
        } else {
            providerResponseDto = onNonEmptyProviders(requestDto, requestDto.getAccountNumber());
        }

        ResponseDto responseDto = new ResponseDto();
        responseDto.setResult(providerResponseDto);
        return responseDto;
    }

    private ProviderResponseDto[] onEmptyProviders(String accountNumber) throws CustomException{
        int i=0;
        ProviderResponseDto[] providerResponseDto = new ProviderResponseDto[providerProperties.getUrl().size()];

        for (Map.Entry<String, String> entry : providerProperties.getUrl().entrySet()) {
            try {
                providerResponseDto[i++] = makeRequest(entry.getValue(), accountNumber, entry.getKey());
                LOG.info("{} response: {}",entry.getKey(), providerResponseDto[i - 1]);
            } catch (URISyntaxException | JSONException | IOException e) {
                LOG.error("{} response: {}",entry.getKey(), e.getMessage());
                throw new CustomException("While processing " + entry.getKey()+" encountered error: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return providerResponseDto;
    }

    private ProviderResponseDto[] onNonEmptyProviders(RequestDto requestDto, String accountNumber) throws CustomException{
        int i=0;
        ProviderResponseDto[] providerResponseDto = new ProviderResponseDto[requestDto.getProviders().length];

        for (String providerName : requestDto.getProviders()) {
            String url = providerProperties.getUrl().get(providerName);
            if (url == null) {
                throw new CustomException("Url not found for provider: " + providerName, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            try {
                providerResponseDto[i++] = makeRequest(url, accountNumber, providerName);
                LOG.info("{} response: {}",providerName, providerResponseDto[i - 1]);
            } catch (URISyntaxException | JSONException | IOException e) {
                LOG.error("{} response: {}",providerName, e.getMessage());
                if(e instanceof SocketTimeoutException)
                    throw new CustomException("While processing " + providerName+" encountered error: "+e.getMessage(), HttpStatus.REQUEST_TIMEOUT);
                throw new CustomException("While processing " + providerName+" encountered error: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return providerResponseDto;
    }


    public ProviderResponseDto makeRequest(String url, String accountNumber, String providerName) throws URISyntaxException, IOException, JSONException,CustomException {
        LOG.info("makeRequest started with url: {} , accountNumber: {} and providerName: {} ", url,accountNumber,providerName);
        HttpEntityEnclosingRequestBase httpEntity = new HttpEntityEnclosingRequestBase() {
            @Override
            public String getMethod() {
                return HttpMethod.GET.name();
            }
        };
        httpEntity.setURI(new URI(url));
            httpEntity.setEntity(new StringEntity(new JSONObject().put("accountNumber", accountNumber).toString(), ContentType.APPLICATION_JSON));
            final RequestConfig config = RequestConfig.custom().setConnectTimeout(1000).setSocketTimeout(1000).build();
            HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

            HttpResponse response = httpClient.execute(httpEntity);
            String jsonResponse = EntityUtils.toString(response.getEntity());

            if (response.getStatusLine().getStatusCode() == org.apache.http.HttpStatus.SC_OK && jsonResponse!=null) {
                org.apache.http.HttpEntity entity = response.getEntity();
                ObjectMapper om = new ObjectMapper();
                return new ProviderResponseDto(providerName, om.readValue(jsonResponse, Boolean.class));
            } else {
                LOG.error("Http response: {} && code : {}",response, response.getStatusLine().getStatusCode());
                throw new CustomException("Provider sever response:"+response, HttpStatus.valueOf(response.getStatusLine().getStatusCode()));
            }
    }

    /*private ProviderResponseDto makeRequest3(String url, String accountNumber, String providerName) throws URISyntaxException, IOException {
        URI uri = new URI(url);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("accountNumber", accountNumber);
        HttpEntity<?> requestEntity = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Boolean.class);
        return new ProviderResponseDto(providerName, response.getBody());
    }*/
}
