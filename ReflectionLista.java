import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionLista {

    // Atributo do tipo lista: ele NÃO é criado com "new", e sim via reflection
    private List<String> nomes;

    // 1) Instancia a lista via reflection: Class -> Constructor -> newInstance()
    @SuppressWarnings("unchecked")
    public void instanciarLista() throws ReflectiveOperationException {
        Class<?> classe = Class.forName("java.util.ArrayList");
        Constructor<?> construtor = classe.getDeclaredConstructor();
        this.nomes = (List<String>) construtor.newInstance();
    }

    // 2) Adiciona um objeto invocando o método add via reflection (sem chamar nomes.add())
    public void adicionar(Object valor) throws ReflectiveOperationException {
        Method add = nomes.getClass().getMethod("add", Object.class);
        add.invoke(nomes, valor);
    }

    // 3) Itera a lista via reflection: iterator() -> hasNext() -> next() (sem for-each)
    public void exibir() throws ReflectiveOperationException {
        Method iterator = nomes.getClass().getMethod("iterator");
        Object iterador = iterator.invoke(nomes);

        // Os métodos são buscados na interface pública Iterator, pois a classe real do
        // iterador é interna e privada do ArrayList, e não pode ser acessada diretamente
        Class<?> interfaceIterator = Class.forName("java.util.Iterator");
        Method hasNext = interfaceIterator.getMethod("hasNext");
        Method next = interfaceIterator.getMethod("next");

        while ((Boolean) hasNext.invoke(iterador)) {
            Object valor = next.invoke(iterador);
            System.out.println(valor);
        }
    }

    public static void main(String[] args) {
        ReflectionLista exemplo = new ReflectionLista();

        try {
            exemplo.instanciarLista();

            exemplo.adicionar("Ana");
            exemplo.adicionar("Bruno");
            exemplo.adicionar("Carla");

            System.out.println("Valores da lista (percorrida via reflection):");
            exemplo.exibir();
        } catch (ReflectiveOperationException e) {
            System.out.println("Erro ao usar reflection: " + e.getMessage());
        }
    }
}
