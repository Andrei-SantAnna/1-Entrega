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
Feito a instalação, agora vamos direto no site do GITHUB e copiar o link do repositório. Depois de copiado, acesse a pasta onde deseja que seja salvo os arquivos. Com o botão direito do mouse vai aparecer essas informações, acesse o open terminal
<img width="224" height="243" alt="Screenshot from 2025-09-30 14-31-46" src="https://github.com/user-attachments/assets/bdaa2bcf-f9ac-4b3b-a815-1ae4a042a9f6" />

Dentro do prompt digite a linha de comando:git clone https://github.com/Andrei-SantAnna/1-Entrega.git

Depois entre na pasta do repositório criado
cd 1-Entrega

Caso não funcione via clone do git, também existe a opção de baixar o ZIP do Github, volte para a página web do Github e em code<> selecione para baixar o ZIP

<img width="398" height="229" alt="Screenshot from 2025-09-30 14-33-52" src="https://github.com/user-attachments/assets/126fe579-b93b-4431-b240-0b4dbf9331f8" />

Depois de baixado talvez seja necessário compilar novamente as classes, como já foi exportado com a versão funcionando, talvez seja necessário quando quiser fazer alguma alteração utilize o comando
javac nome_da_classe.java
Assim poderá compilar a classe que deseja.
- **Executando o Servidor**
Após a compilação, você pode iniciar o servidor. Você precisará de dois terminais abertos na pasta do projeto 1 para o Servidor e o outro para o Cliente.
comando: java Servidor.java <porta que quiser para o cliente> <porta que quiser para o outro servidor> <nome do arquivo de perguntas> como mostrado no prompt abaixo

<img width="589" height="28" alt="Screenshot from 2025-09-30 14-44-02" src="https://github.com/user-attachments/assets/0903822f-439d-4741-8ec8-6b11563aab3b" />

- **Executando o Cliente**
Abra um segundo terminal para jogar, conectando-se ao servidor.
comando - java Cliente.java <nome do host - localhost> <porta escolhida lá no comando do servidor para o cliente>
- Comandos disponíveis no cliente: listar_temas, jogar <tema>, sair.
E vualá, você já vai conseguir jogar os jogos de perguntas, foram criadas 3 temas, Futebol, Historia e Conhecimentos Gerais

# Desafios encontrados
Durante o desenvolvimento, alguns desafios técnicos surgiram, servindo como importantes oportunidades de aprendizado.
Uma delas foi a troca de dados entre servidores distintos:
- No caso foi: Como transferir uma lista inteira de objetos Questao de um servidor para outro de forma eficiente e sem a necessidade de bibliotecas externas.
Outro desafio foi Erros na Lógica de Verificação de Respostas:
- Após as refatorações do protocolo, o sistema passou a considerar todas as respostas dos jogadores como incorretas.Foi necessário rastrear o fluxo da resposta do jogador: desde a entrada da letra no cliente, sua conversão para índice numérico, o envio via socket, e a recepção e comparação no servidor.

# Soluções pensadas
Troca de Dados Complexos entre Servidores:
- Foi adotada a serialização de objetos nativa do Java. A classe Questao implementou a interface java.io.Serializable, permitindo que a lista de objetos fosse convertida em um fluxo de bytes, enviada via ObjectOutputStream e reconstruída no destino com ObjectInputStream. Isso manteve o código alinhado aos requisitos e ao paradigma de orientação a objetos.
  
Erros na Lógica de Verificação de Respostas:

- Foi adicionada uma linha de depuração (System.out.println) estratégica no servidor (ConexaoCliente) para imprimir o valor recebido do cliente e o valor esperado. Isso permitiu visualizar a discrepância em tempo real e identificar uma falha na lógica de validação do comando RESPOSTA, que foi prontamente corrigida. A técnica demonstrou a importância de ferramentas simples de log para a depuração de sistemas distribuídos.











