# 2 Unidade - 1-Entrega 
# Discente: Andrei Boulhosa de Sant'Anna

# Sobre o projeto
- Esse Projeto é uma implementação de uma comunicação Servidor-Cliente distribuída, onde envolve um jogo de perguntas e respostas(QUIZ). Objetivo principal é simular a expansão de conhecimento da comunicação de uma rede, onde cada nó é independente, porém que possa cooperar com outros equipamentos para enriquecer sua própria base de dados. O sistema foi criado utilizando a linguagem Java, utilizando o método Socket para a comunicação entre redes (Servidor-Servidor) e em rede(Servidor-Cliente) e a serialização que é nativa da linguagem, para a troca de dados, sem a necessidade de bibliotecas externas.

# Funcionamento
-  O sistema pensado foi dividido em componentes que se comunicam através de protocolos de redes bem definidos

- Servidor.java: É o coração do sistema. Cada servidor opera de forma independente, gerenciando seu próprio BancoPerguntas. Ele é multithreaded e possui duas portas de escuta:

    Uma porta para a conexão de múltiplos Clientes (jogadores).

    Uma porta para a conexão de outros Servidores (parceiros ou peers) para troca de informações.

- Cliente.java: É a interface do jogador, executada via terminal. Ele se conecta a um servidor para listar temas e jogar os quizzes. A interface foi projetada para ser intuitiva, utilizando letras (a, b, c...) para as respostas.

- BancoPerguntas.java: Classe responsável por gerenciar as questões em memória. Carrega as perguntas iniciais de um arquivo de texto (.txt) e adiciona novas questões recebidas de outros servidores, evitando duplicatas.

- ConexaoCliente.java: Uma thread gerada pelo Servidor para cada jogador que se conecta. Ela gerencia toda a lógica de uma sessão de jogo.

- ConexaoServidor.java: Uma thread gerada pelo Servidor para lidar com requisições de outros servidores que desejam "parear" e obter a base de questões.

- Protocolos de Comunicação - Dentro do projeto foram definidos alguns comandos para facilitar a comunicação
- Protocolos Servidor-Cliente
<img width="943" height="183" alt="Screenshot from 2025-09-30 09-46-07" src="https://github.com/user-attachments/assets/ecdf08bf-1fa2-41e7-b384-18c044735094" />

- Protocolos Servidor-Servidor via pareamento
  
<img width="803" height="87" alt="Screenshot from 2025-09-30 09-51-56" src="https://github.com/user-attachments/assets/973cd70f-7861-48c5-90c0-6b145bfbedfc" />

# Como Executar
Para gerar o código é bem simples, no tutorial abaixo vou auxilia-lo a baixar o código para que possa verificar os resultados e até mesmo fazer suas próprias alterações. Para que os códigos funcionem será necessário ter instalado em sua máquina:
- Java Development Kit (JDK) 8 ou superior instalado.
- Git instalado (para baixar o repositório).
Feito a instalação, agora vamos direto no site do GITHUB e copiar o link do repositório

##ddddd

















