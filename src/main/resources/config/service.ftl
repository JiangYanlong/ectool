package ${package};

import weaver.hrm.User;

import java.util.Map;

public interface ${className}Service {

    <#list lists as li>
    Map<String, Object> generate${li.random}(Map<String, Object> params, User user);
    </#list>
}

