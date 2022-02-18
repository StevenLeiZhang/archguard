## 获取所有package的依赖
+ ##### URL地址: `/package/dependence/all`
+ ##### 请求方式: `GET`

+ ##### response:
    ###### 样例
    ```Json
  {
      "nodes": [
        {
          "id": 1,
          "name": "application",
          "parent": 0
        },
        {
          "id": 2,
          "name": "fix",
          "parent": 1
        }
      ],
      "edges": [
        {
          "a": 1,
          "b": 2,
          "num": 10
        }
      ]
  }
    ```
  
    ###### 详细
    字段 | 说明 | 类型 | 备注 | 是否可空
    :----:|:---:|:---:|:---:|:---:
    `nodes` | 节点 | `数组` | | N
    `edges` | 依赖关系 | `数组` | | N
    `id` | 唯一标识 | `int` | 后台按顺序生成 | N
    `name` | 包名 | `String` |只包含最后一级，不包含于parent重合部分| N
    `parent` | 上一级包名 | `int` | | N
    `a` | 调用节点 | `int` | | N
    `b` | 被调用节点 | `int` | | N
    `num` | 依赖数量 | `int` | 方法的调用数量 | N

+ #### Status
    状态码 | 说明 | 备注
    :---:|:---:|:---:
    `200` | 正常返回 | 


## 获取所有module的依赖
+ ##### URL地址: `/module/dependence/all`
+ ##### 请求方式: `GET`

+ ##### response:
    ###### 样例
    ```Json
  {
      "nodes": [
        {
          "id": 1,
          "name": "code-analysis-scanner"
        },
        {
          "id": 2,
          "name": "code-analysis-addition"
        }
      ],
      "edges": [
        {
          "a": 1,
          "b": 2,
          "num": 10
        }
      ]
  }
    ```
  
    ###### 详细
    字段 | 说明 | 类型 | 备注 | 是否可空
    :----:|:---:|:---:|:---:|:---:
    `nodes` | 节点 | `数组` | | N
    `edges` | 依赖关系 | `数组` | | N
    `id` | 唯一标识 | `int` | 后台按顺序生成 | N
    `name` | module名 | `String` | | N
    `a` | 调用节点 | `int` | | N
    `b` | 被调用节点 | `int` | | N
    `num` | 依赖数量 | `int` | 方法的调用数量 | N

+ #### Status
    状态码 | 说明 | 备注
    :---:|:---:|:---:
    `200` | 正常返回 |


## 新增项目信息
+ ##### URL地址: `/project/info`
+ ##### 请求方式: `POST`

+ ##### 请求:
    ###### 样例
    ```Json
  {
      "projectName": "spring", 
      "repo": ["https://github.com/spring-projects/spring-framework.git"],
      "repoType": "GIT"  
  }
    ```
  
    ###### 详细
    字段 | 说明 | 类型 | 备注 | 是否可空
    :----:|:---:|:---:|:---:|:---:
    `projectName` | 项目名称 | `String` | | N
    `repo` | 项目仓库地址 | `String` | | N
    `repoType` | 项目仓库类型 | `String` | | N
    
    
+ ##### response:
    ###### 样例
    ```Json
    {
      "success": true,
      "message": "add new project info success",
      "id": "c06da91f-6742-11ea-8188-0242ac110002"
    }
    ```

    ###### 详细
    字段 | 说明 | 类型 | 备注 | 是否可空
    :---:|:---:|:---:|:---:|:---:
    `success` | 是否成功 | `Boolean` | | N
    `message` | 返回信息 | `String` | | N

+ #### Status
    状态码 | 说明 | 备注
    :---:|:---:|:---:
    `200` | 正常返回 | 
    

## 查询项目信息
+ ##### URL地址: `/project/info`
+ ##### 请求方式: `GET`

+ ##### response:
    ###### 样例
    ```Json
  {
      "id": "c06da91f-6742-11ea-8188-0242ac110002",
      "projectName": "spring", 
      "repo": ["https://github.com/spring-projects/spring-framework.git"],
      "repoType": "GIT"
  }
    ```
  
    ###### 详细
    字段 | 说明 | 类型 | 备注 | 是否可空
    :----:|:---:|:---:|:---:|:---:
    `projectName` | 项目名称 | `String` | | N
    `repo` | 项目仓库地址 | `String` | | N
    `repoType` | 项目仓库类型 | `String` | | N
    
    
+ #### Status
    状态码 | 说明 | 备注
    :---:|:---:|:---:
    `200` | 正常返回 | 
    
## 修改项目信息
+ ##### URL地址: `/project/info`
+ ##### 请求方式: `PUT`

+ ##### 请求:
    ###### 样例
    ```Json
  {
      "id": "c06da91f-6742-11ea-8188-0242ac110002",
      "projectName": "spring", 
      "repo": ["https://github.com/spring-projects/spring-framework.git"],
      "repoType": "GIT"  
  }
    ```
  
    ###### 详细
    字段 | 说明 | 类型 | 备注 | 是否可空
    :----:|:---:|:---:|:---:|:---:
    `projectName` | 项目名称 | `String` | | N
    `repo` | 项目仓库地址 | `String` | | N
    `repoType` | 项目仓库类型 | `String` | | N
    
    
+ ##### response:
    ###### 样例
    ```Json
    {
      "success": true,
      "message": "update project info success"
    }
    ```

    ###### 详细
    字段 | 说明 | 类型 | 备注 | 是否可空
    :---:|:---:|:---:|:---:|:---:
    `success` | 是否成功 | `Boolean` | | N
    `message` | 返回信息 | `String` | | N

+ #### Status
    状态码 | 说明 | 备注
    :---:|:---:|:---:
    `200` | 正常返回 | 
    
    
## 获取checkstyle概览
+ ##### URL地址: `/checkstyle/overview`
+ ##### 请求方式: `GET`

+ ##### response:
    ###### 样例
    ```Json
    [
      {
         "message": "Line is longer than 80 characters (found 82).",
         "count": 1
      }
    ]
    ```

    ###### 详细
    字段 | 说明 | 类型 | 备注 | 是否可空
    :---:|:---:|:---:|:---:|:---:
    `message` | 检查的类型信息 | `String` | | N
    `count` | 总数 | `Int` | | N

+ #### Status
    状态码 | 说明 | 备注
    :---:|:---:|:---:
    `200` | 正常返回 | 
    
## 获取信息
+ ##### URL地址: `/reports/overview`
+ ##### 请求方式: `GET`

+ ##### response:
    ###### 样例
    ```Json
    {
      "codeLines": 181811,
      "commitCount": 0,
      "contributors": 0
    }
    ```

    ###### 详细
    字段 | 说明 | 类型 | 备注 | 是否可空
    :---:|:---:|:---:|:---:|:---:
    `codeLines` | 代码行数 | `Int` | | N
    `commitCount` | git提交次数 | `Int` | | N
    `contributors` | git贡献者的数量 | `Int` | | N

+ #### Status
    状态码 | 说明 | 备注
    :---:|:---:|:---:
    `200` | 正常返回 | 