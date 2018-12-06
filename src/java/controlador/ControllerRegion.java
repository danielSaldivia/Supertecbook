/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import Model.Comuna;
import Model.Region;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author yo
 */
@Controller
public class ControllerRegion {

    @RequestMapping(value="/region",method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        modelMap.put("region", new Region());
        return "region";
    }
}

  