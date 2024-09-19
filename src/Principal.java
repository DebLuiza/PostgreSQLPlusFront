import static spark.Spark.*;
import com.google.gson.Gson;
import java.util.*;

public class Principal {
    private static final Gson gson = new Gson();
    private static final UsuarioDAO dao = new UsuarioDAO();

    public static void main(String[] args) {
        port(4567); // Porta do servidor
        
        staticFiles.location("/public");

        // Configurar Spark para aceitar e responder em JSON
        before((req, res) -> res.type("application/json"));

        // Rota para listar todos os usuários
        get("/usuarios", (req, res) -> {
            List<Usuario> lista = dao.listar();
            return gson.toJson(lista);
        });

        // Rota para inserir um usuário
        post("/usuarios", (req, res) -> {
            Usuario usuario = gson.fromJson(req.body(), Usuario.class);
            dao.inserir(usuario);
            res.status(201); // Código de status HTTP 201 (Criado)
            return "{\"status\":\"Usuário criado com sucesso\"}";
        });

        // Rota para excluir um usuário
        delete("/usuarios/:codigo", (req, res) -> {
            int codigo = Integer.parseInt(req.params(":codigo"));
            dao.excluir(codigo);
            return "{\"status\":\"Usuário excluído com sucesso\"}";
        });

        // Rota para atualizar um usuário
        put("/usuarios/:codigo", (req, res) -> {
            int codigo = Integer.parseInt(req.params(":codigo"));
            Usuario usuarioAtualizado = gson.fromJson(req.body(), Usuario.class);
            usuarioAtualizado.setCodigo(codigo);
            dao.atualizar(usuarioAtualizado);
            return "{\"status\":\"Usuário atualizado com sucesso\"}";
        });

        // Página inicial
        get("/", (req, res) -> {
            res.redirect("/index.html");
            return null;
        });

    }
}
