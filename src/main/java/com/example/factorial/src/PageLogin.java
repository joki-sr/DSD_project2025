package com.example.factorial.src;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PageLogin extends Facade {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    //初始化，为每一个页面实例添加权限用户
    //此页面作为初始界面，不需要赋予权限
    @Override
    public void INTI(User user){
        this.user = user;
    }
    //登录方法
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        
        try {
            // 从数据库中查询用户信息
            String sql = "SELECT COUNT(*) FROM dsd.users WHERE username = ? AND password = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username, password);
            
            if (count != null && count > 0) {
                return ResponseEntity.ok("登录成功");
            } else {
                return ResponseEntity.status(401).body("用户名或密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("服务器错误: " + e.getMessage());
        }
    }
    @PostMapping("/Signup")
    public ResponseEntity<String>  Signup(@RequestBody SignupRequest request){
        String username = request.getUsername();
        String password = request.getPassword();
        /*检验是否是患者账号类型*/
        boolean ex = true;
        if(ex){
            return ResponseEntity.ok("成功注册患者账号");
        }
        else{
            return ResponseEntity.status(401).body("用户名格式不对");
        }
    }
    //静态添加若干方法
}
