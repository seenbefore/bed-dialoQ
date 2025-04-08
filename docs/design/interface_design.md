### 4. 用户管理接口

#### 4.1 用户管理

##### 4.1.1 用户登录
- 请求路径：`/api/auth/login`
- 请求方法：POST
- 请求参数：
  ```json
  {
    "username": "用户名",
    "password": "密码"
  }
  ```
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "token": "访问令牌",
      "user": {
        "id": "用户ID",
        "username": "用户名",
        "realName": "真实姓名",
        "avatar": "头像URL",
      }
    }
  }
  ```

##### 4.1.2 用户登出
- 请求路径：`/api/auth/logout`
- 请求方法：POST
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "success"
  }
  ```

##### 4.1.3 获取当前用户信息
- 请求路径：`/api/auth/current`
- 请求方法：GET
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": "用户ID",
      "username": "用户名",
      "realName": "真实姓名",
      "email": "邮箱",
      "phone": "手机号",
      "avatar": "头像URL",
      "lastLoginTime": "最后登录时间"
    }
  }
  ```

##### 4.1.4 修改密码
- 请求路径：`/api/auth/password`
- 请求方法：POST
- 请求参数：
  ```json
  {
    "newPassword": "新密码"
  }
  ```
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "success"
  }
  ``` 

##### 4.1.5 修改用户信息
- 请求路径：`/api/auth/update`
- 请求方法：POST
- 请求参数：
  ```json
  {
    "id": "用户ID",
    "username": "用户名",
    "realName": "真实姓名",
    "email": "邮箱",
    "phone": "手机号",
    "avatar": "头像URL"
  }
  ```
- 响应结果：
  ```json
  {
    "code": 200,
    "message": "success"
  }
  ```
