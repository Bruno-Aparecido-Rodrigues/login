package io.github.fatec.configuration;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Hints de reflexão para o build nativo (GraalVM).
 * <ul>
 *   <li>jjwt 0.12 carrega a implementação por reflexão (Classes.newInstance("io.jsonwebtoken.impl...")),
 *       então todas as classes de io.jsonwebtoken são registradas. Sem isso dá UnknownClassException no nativo.</li>
 *   <li>O driver do MongoDB usa JNDI/DNS para resolver URIs mongodb+srv://.</li>
 * </ul>
 * Executado só em build (fase AOT); não tem custo em tempo de execução.
 */
public class NativeHints implements RuntimeHintsRegistrar {

    private static final MemberCategory[] ALL = {
            MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
            MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
            MemberCategory.INVOKE_DECLARED_METHODS,
            MemberCategory.INVOKE_PUBLIC_METHODS,
            MemberCategory.DECLARED_FIELDS,
            MemberCategory.PUBLIC_FIELDS
    };

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        registerAllClasses(hints, classLoader, "io/jsonwebtoken/");
        hints.reflection().registerType(TypeReference.of("com.sun.jndi.dns.DnsContextFactory"), ALL);
    }

    private void registerAllClasses(RuntimeHints hints, ClassLoader classLoader, String basePath) {
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver(classLoader)
                    .getResources("classpath*:" + basePath + "**/*.class");
            for (Resource resource : resources) {
                String url = resource.getURL().toString();
                int start = url.lastIndexOf(basePath);
                if (start < 0) {
                    continue;
                }
                String className = url.substring(start, url.length() - ".class".length()).replace('/', '.');
                if (className.endsWith("module-info")
                        || className.endsWith("package-info")
                        || className.matches(".*\\$\\d+.*")) { // classes anônimas
                    continue;
                }
                hints.reflection().registerType(TypeReference.of(className), ALL);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
