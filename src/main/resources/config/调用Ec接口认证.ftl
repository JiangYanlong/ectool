### 调用Ec接口认证

💡调用Ec接口需要获取Ec系统的Token，访问具体业务接口时需要把获取到的Token放入到接口**Header**中，key为 **token** ，Ec会提供具体的业务调用 **APPID** ，也需要放入到接口的Header中，key为 **appid**

Ec提供的参数如下：

| appid        | ${appid}  |
| ------------ | --------- |
| 系统访问地址 | ${url}    |
| spk          | ${spk}    |
| secret       | ${secret} |

#### 1. 获取Token

- 访问地址

  ${accessTokenUrl}

- 请求方式

  POST

- 请求参数（Header）

  | 参数   | 类型   | 说明                                                         |
  | ------ | ------ | ------------------------------------------------------------ |
  | appid  | String | Ec提供的**APPID** (${appid})                                 |
  | secret | String | 用Ec提供的**spk** (${spk})、**secret** (${secret}) 参数，通过RSA加密算法计算 |

- 返回结构

  ```json
  {"msg":"获取成功!","code":0,"msgShowType":"none","status":true,"token":"7099a965-7ef4-4e2d-a1e2-a4d66a10e945"}
  ```

- 结构说明

  code = 0 时代表获取Token成功，token为具体的值

- secret 加密算法示例

  ```java
  RSA rsa = new RSA(null,"${spk}");
  String encryptSecret = rsa.encryptBase64("${secret}", CharsetUtil.CHARSET_UTF_8, KeyType.PublicKey);
  String data = HttpRequest.post("${accessTokenUrl}")
              .header("appid","${appid}")
              .header("secret",encryptSecret)
              .header("time","3600")
              .execute().body();
  ```

#### 2. 具体业务接口调用，如创建流程

- 访问地址

  ${createFlowUrl}

- 请求方式

  application/x-www-form-urlencoded

- 请求参数（Header）

  | 参数   | 类型   | 说明                                                         |
  | ------ | ------ | ------------------------------------------------------------ |
  | appid  | String | Ec提供的**APPID** (${appid})                                 |
  | token  | String | 获取Token接口返回的值                                        |
  | userid | String | 用Ec提供的**spk** (${spk}) 、**userid** (${userid}) 参数，通过RSA加密算法计算 |

- secret 加密算法示例

  ```java
  RSA rsa = new RSA(null,"${spk}");
  String userid = rsa.encryptBase64("${userid}",CharsetUtil.CHARSET_UTF_8,KeyType.PublicKey);
  ```

- 请求参数（Body）

  | **参数名**       | **类型** | **必填** | **描述**                                                     | **示例** |
  | ---------------- | -------- | -------- | ------------------------------------------------------------ | -------- |
  | **detailData**   | String   | 非必填   | 明细表数据，例子请看注意事项                                 |          |
  | **mainData**     | String   | 必填     | 主表数据，例子请看注意事项                                   |          |
  | **otherParams**  | String   | 非必填   | 其他参数，比如messageType,isnextflow,requestSecLevel，delReqFlowFaild |          |
  | **remark**       | String   | 非必填   | 签字意见，默认值流程默认意见若未设置则为空                   |          |
  | **requestLevel** | String   | 非必填   | 紧急程度                                                     |          |
  | **requestName**  | String   | 必填     | 流程标题                                                     |          |
  | **workflowId**   | Int      | 必填     | 流程Id                                                       |          |

  detailData 示例

  ```json
  [
    {
      "tableDBName": "formtable_main_1356_dt1",
      "workflowRequestTableRecords": [
        {
          "recordOrder": 0,
          "workflowRequestTableFields": [
            {
              "fieldName": "dhwb",
              "fieldValue": "第一行"
            },
            {
              "fieldName": "drl",
              "fieldValue": "2978"
            },
            {
              "fieldName": "xlk",
              "fieldValue": "0"
            }
          ]
        },
        {
          "recordOrder": 0,
          "workflowRequestTableFields": [
            {
              "fieldName": "dhwb",
              "fieldValue": "第2行"
            },
            {
              "fieldName": "drl",
              "fieldValue": "2978"
            },
            {
              "fieldName": "xlk",
              "fieldValue": "1"
            }
          ]
        }
      ]
    }
  ]
  ```

  mainData 示例

  ```json
  [
    {
      "fieldName": "dhwb",
      "fieldValue": {
        "value": "1212",
        "name": "aaaa.html"
      }
    },
    {
      "fieldName": "drlzy",
      "fieldValue": "2978"
    },
    {
      "fieldName": "drlzy1",
      "fieldValue": "2978,2979"
    },
    {
      "fieldName": "fjsc",
      "fieldValue": [
        {
          "filePath": "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1577426926378&di=0084fc19f5cb90fc2286aab5ca8c343e&imgtype=0&src=http%3A%2F%2Fpic.51yuansu.com%2Fpic2%2Fcover%2F00%2F41%2F80%2F581369c715701_610.jpg",
          "fileName": "timg.jpg"
        },
        {
          "filePath": "http://192.168.7.216:8080/weaver/weaver.file.FileDownload?download=1&fileid=3819049&authStr=dmlld0NoYWluPTIyNTkzOTA1fG1haW5pZD0yMjU5MzkwNXw=&authSignatureStr=81b08f6e0b8ee06dcc1b43fda8204bdf&requestid=22593905&f_weaver_belongto_userid=4548&f_weaver_belongto_usertype=0",
          "fileName": "自由节点视图mysql.txt"
        }
      ]
    }
  ]
  ```

- 返回值类型

​		JSON

- 返回示例

  ```json
  {
    "code": "SUCCESS",
    "data": {
      "requestid": 2966060
    },
    "errMsg": {}
  }	
  ```

- 返回参数说明

  | **code**   | String | 返回数据状态 SUCCESS：成功，PARAM_ERROR：参数错误，NO_PERMISSION：无权限，SYSTEM_INNER_ERROR：程序异常，USER_EXCEPTION：用户异常 |
  | ---------- | ------ | ------------------------------------------------------------ |
  | **data**   | Json   | 接口状态为SUCCESS,则data中包含生成的requestid                |
  | **errMsg** | Json   | 接口异常信息：例如状态为PARAM_ERROR 则返回错误参数信息       |

- 注意事项

  > 1、如果新建流程参数不符合规则、比如mainData 格式错误多个“}、]”则会提示参数错误，一般会提示workflowId为0 2、必填字段不允许为空 3、参数workflowId对应的流程要有该流程创建权限 4、主表、明细表中如果参数不正确（和路径表单字段 字段名称不一致，参数中多了都不允许正常新建流程，并且会提示异常参数） 5、附件上传说明，字段值格式如下： 格式为 [{"fieldName":"字段数据库名称","fieldValue":字段值}] 例如：附件上传字段的值为 fieldValue:[{"filePath":"上传的附件地址或者base64","fileName":"附件名称(包含附件类型)"}] 除了附件上传，其余字段类型的字段值都为字段内容 6、otherParams参数为map，扩展参数暂时支持如下参数： isnextflow ：新建流程是否默认提交到第二节点，可选值为[0 ：不流转 1：流转 (默认)] delReqFlowFaild：新建流程失败是否默认删除流程，可选值为[0 ：不删除 1：删除 (默认)] requestSecLevel: "流程密级， 开启密级后生效， 默认公开",