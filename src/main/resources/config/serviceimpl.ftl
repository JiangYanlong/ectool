package ${package};
<#list lists as li>
import com.engine.${path}.cmd.${li.cmdname}Cmd;
</#list>
import com.engine.${path}.service.${className}Service;
import com.engine.core.impl.Service;
import weaver.hrm.User;

import java.util.Map;

public class ${className}ServiceImpl extends Service implements ${className}Service {

    <#list lists as li>
    @Override
    public Map<String, Object> generate${li.random}(Map<String, Object> params, User user) {
        return commandExecutor.execute(new ${li.cmdname}Cmd(user, params));
    }
    </#list>
}

