package iam.aws.admin;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import iam.aws.core.EmergencyH1;
import iam.aws.core.EmergencyH2;
import iam.aws.core.EmergencyH3;
import iam.aws.core.HandlerH1;
import iam.aws.core.HandlerH2;
import iam.aws.enums.Access;
import iam.aws.model.Handler;

@Service
public class ChainManager {
    public final Map<Access, List<Handler>> chainMap = new EnumMap<>(Access.class);

    public ChainManager(EmergencyH1 e1, EmergencyH2 e2, EmergencyH3 e3, HandlerH1 h1, HandlerH2 h2) {
        chainMap.put(Access.EMERGENCY, List.of(e3, e1, e2));
        chainMap.put(Access.NORMAL, List.of(h1, h2));
    }

    public List<Handler> chainHandlers(Access access) {
        return chainMap.get(access);
    }
}
