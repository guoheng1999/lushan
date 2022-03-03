package edu.cuit.lushan.controller;


import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Guoheng
 * @since 2022-02-26
 */
@Controller
@RequestMapping("/userProof")
@RequiresRoles({"USER"})
@CrossOrigin
public class UserProofController {

}

