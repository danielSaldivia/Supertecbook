/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import ControladorJpa.ComunaJpaController;
import Model.Comuna;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author yo
 */
@Controller
public class ControllerComuna {


    @RequestMapping(value = "/comuna", method = RequestMethod.GET)
    public ModelAndView showForm() {
        return new ModelAndView("comuna", "comuna", new Comuna());
    }

    @RequestMapping(value = "/addcomuna", method = RequestMethod.POST)
    public String submit(@Valid @ModelAttribute("comuna") Comuna com,
            BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "error";
        }
        model.addAttribute("nombre", com.getNombre());
        model.addAttribute("codigo", com.getCodigo());
        return "addcomuna";
    }



}
