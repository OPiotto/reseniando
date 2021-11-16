package com.reseniando.grupo4.controladores;

import com.reseniando.grupo4.enumeraciones.Generos;
import com.reseniando.grupo4.errores.ErrorServicio;
import com.reseniando.grupo4.servicios.LibroServicio;
import com.reseniando.grupo4.servicios.UsuarioServicio;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private LibroServicio libroServicio;

    @GetMapping("/")
    public String index(ModelMap model) {
        ArrayList<String> todosLosGeneros = new ArrayList();
        String aux;
        for (Generos nombreGen : Generos.values()) {
            aux = nombreGen.getGen();
            todosLosGeneros.add(aux);
        }

        model.addAttribute("libros", libroServicio.listarDestacados());
        model.addAttribute("genero", todosLosGeneros);

        return "index.html";
    }

//    @GetMapping("/lista")
//    public String lista(ModelMap modelo) {
//
//            List<Perro> perrosLista = perroService.listarTodos();
//
//            modelo.addAttribute("perros",perrosLista);		
//            return "list-perro";
//    }
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap model) {
        if (error != null) {
            model.put("error", "Credenciales invalidas. Intente nuevamente.");
        }
        if (logout != null) {
            model.put("logout", "Se ha cerrado la sesion.");
        }
        return "login";
    }

    @GetMapping("/registro")
    public String registro(ModelMap modelo) {
        return "registro";
    }

    @PostMapping("/registrar")
    public String registrar(
            ModelMap modelo,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String mail,
            @RequestParam String pass1,
            @RequestParam String pass2,
            @RequestParam String dni,
            @RequestParam String direccion
    ) {
        try {
            usuarioServicio.crearUsuario(dni, nombre, apellido, direccion, mail, pass1, pass2);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("mail", mail);
            modelo.put("password1", pass1);
            modelo.put("password2", pass2);
            modelo.put("dni", dni);
            modelo.put("direccion", direccion);

            return "registro";
        }

        modelo.put("titulo", "¡Bienvenido a Reseniando!");
        modelo.put("descripcion", "Tu usuario fue registrado correctamente.");

        return "exito";
    }
}
