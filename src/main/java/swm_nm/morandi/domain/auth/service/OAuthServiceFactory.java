package swm_nm.morandi.domain.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OAuthServiceFactory {

    private final Map<String, OAuthService> serviceMap;

    @Autowired
    public OAuthServiceFactory(List<OAuthService> oAuthServices) {

        //List로 들어오는 Bean 객체들을 -> Map으로 변환하여 등록
        this.serviceMap = oAuthServices.stream()
                .collect(Collectors.toMap(OAuthService::getType, Function.identity()));
    }

    public OAuthService getServiceByType(String type) {
        return serviceMap.get(type);
    }
}
