package ${package};

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import weaver.hrm.User;

import java.util.HashMap;
import java.util.Map;

public class ${className}Cmd extends AbstractCommonCommand<Map<String, Object>> {

    public ${className}Cmd(User user, Map<String, Object> params) {
        this.user = user;
        this.params = params;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        // 返回结构
        Map<String, Object> apidatas = new HashMap<String, Object>();
        return apidatas;
    }
}
