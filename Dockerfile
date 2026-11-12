# syntax=docker/dockerfile:1.7
#
# Build nativo com GraalVM (binário estático musl) + runtime Alpine.
#   docker build -t brun0la/login .
#   docker run --rm -p 8080:8080 -e PORT=8080 -e MONGODB_URI="..." brun0la/login
#
# Precisa de ~6 GB de RAM livres para o native-image (o runner do GitHub Actions dá conta).

# ---------- 1) Build: GraalVM com toolchain musl ----------
FROM ghcr.io/graalvm/native-image-community:21-muslib AS build

# Maven copiado da imagem oficial (a imagem do GraalVM não traz Maven)
COPY --from=maven:3.9-eclipse-temurin-21 /usr/share/maven /usr/share/maven
ENV MAVEN_HOME=/usr/share/maven \
    PATH=/usr/share/maven/bin:$PATH

WORKDIR /build
COPY . .

# 1. instala o módulo domain no repositório local
# 2. compila o módulo spring como binário nativo estático (perfis native + musl)
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -ntp -DskipTests -pl domain install && \
    mvn -B -ntp -DskipTests -Pnative,musl -pl spring native:compile

# ---------- 2) Runtime: Alpine ----------
FROM alpine:3.22

RUN addgroup -S app && adduser -S app -G app
WORKDIR /app
COPY --from=build --chown=app:app /build/spring/target/login /app/login

USER app
# O Render sobrescreve PORT automaticamente
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["/app/login"]
