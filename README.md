# 2 Unidade - 1-Entrega 
### Discente: Andrei Boulhosa de Sant'Anna

### Sobre o projeto
- Esse Projeto é uma implementação de uma comunicação Servidor-Cliente distribuída, onde envolve um jogo de perguntas e respostas(QUIZ). Objetivo principal é simular a expansão de conhecimento da comunicação de uma rede, onde cada nó é independente, porém que possa cooperar com outros equipamentos para enriquecer sua própria base de dados. O sistema foi criado utilizando a linguagem Java, utilizando o método Socket para a comunicação entre redes (Servidor-Servidor) e em rede(Servidor-Cliente) e a serialização que é nativa da linguagem, para a troca de dados, sem a necessidade de bibliotecas externas.
### Estrutura do Código
```
comp/
 ├── jogo/
 │    ├── Cliente.class
 │    └── Servidor.class
 ├── network/
 │    ├── ConexaoCliente.class
 │    └── ConexaoServidor.class
 ├── schema/
 │    └── Question.class
 └── util/
      └── BancoPerguntas.class

```
### Regras do Jogo
- O cliente se conecta ao servidor e pode listar os temas disponíveis ou iniciar um jogo.

- Ao iniciar, o cliente recebe as perguntas de um tema, uma de cada vez.

- O cliente responde digitando a letra da alternativa (a, b, c, …) ou um dos comandos (listar_temas, jogar, sair).

- O servidor responde se a resposta está certa ou errada e envia a próxima pergunta.

- Ao final do quiz, o servidor informa a pontuação acumulada naquela sessão.

- O jogo termina quando as perguntas de um tema acabam ou o cliente se desconecta.

- A pontuação é mantida apenas durante a sessão de jogo.

### Funcionamento
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

### Como Executar
Para gerar o código é bem simples, no tutorial abaixo vou auxilia-lo a baixar o código para que possa verificar os resultados e até mesmo fazer suas próprias alterações. Para que os códigos funcionem será necessário ter instalado em sua máquina:
- Java Development Kit (JDK) 8 ou superior instalado.
- Git instalado (para baixar o repositório).
Feito a instalação, agora vamos direto no site do GITHUB e copiar o link do repositório. Depois de copiado, acesse a pasta onde deseja que seja salvo os arquivos. Com o botão direito do mouse vai aparecer essas informações, acesse o open terminal
<img width="224" height="243" alt="Screenshot from 2025-09-30 14-31-46" src="https://github.com/user-attachments/assets/bdaa2bcf-f9ac-4b3b-a815-1ae4a042a9f6" />

Dentro do prompt digite a linha de comando:git clone https://github.com/Andrei-SantAnna/1-Entrega.git

Depois entre na pasta do repositório criado

```bash
cd 1-Entrega
```


Caso não funcione via clone do git, também existe a opção de baixar o ZIP do Github, volte para a página web do Github e em code<> selecione para baixar o ZIP


<img width="398" height="229" alt="Screenshot from 2025-09-30 14-33-52" src="https://github.com/user-attachments/assets/126fe579-b93b-4431-b240-0b4dbf9331f8" />


### Compilação
Compile todos os arquivos para a pasta `comp`:
```bash
javac -d comp $(find src -name "*.java")
````

### Executando o Servidor
Após a compilação, você pode iniciar o servidor. Você precisará de dois terminais abertos na pasta do projeto 1 para o Servidor e o outro para o Cliente.
```bash
java -cp comp jogo.Servidor 6000 7000 src/teste
````
* `6000` → porta do acesso do Cliente
* `7000` → porta do servidor Parceiro
* `teste` → arquivo local de perguntas

### Executando o Cliente
Abra um segundo terminal para jogar, conectando-se ao servidor.
```bash
java -cp comp jogo.Cliente localhost 6000
```
- Comandos disponíveis no cliente: listar_temas, jogar <tema>, sair.
E vualá, você já vai conseguir jogar os jogos de perguntas, foram criadas 3 temas, Futebol, Historia e Conhecimentos Gerais

### Exemplo de Sessão (Cliente)

Conectado ao servidor de jogo!
Comandos: listar_temas, jogar <tema>, sair
> jogar Historia

---------------------------------
Pergunta: Em que ano foi declarada a Independência do Brasil?

a) 1500

b) 1889

c) 1822

d) 1808

---------------------------------
Sua resposta (letra): c
>> CORRETO

### Console de Pareamento
O servidor possui um console administrativo simples no mesmo terminal onde ele roda.

Comandos disponíveis:

PAREAR <host> <porta>: Puxa todas as perguntas de um servidor parceiro e as integra ao banco de dados local.

SAIR: Desliga o servidor.

Exemplo de Pareamento:

No console do Servidor A (que está rodando na porta 50001 para parceiros), 
```bash
java -cp comp jogo.Servidor 50000 50001 src/questoes_servidor1
```

para puxar dados do Servidor B (que está rodando na porta 60001 para parceiros):

```bash
java -cp comp jogo.Servidor 60000 60001 src/teste
```

### Console Administrativo e Pareamento

O servidor possui um console administrativo simples no mesmo terminal onde ele roda.

Comandos disponíveis:

No console do ** Servidor 1**
```
3 questões carregadas de teste
Servidor iniciado. Portas: Clientes=50000, Parceiros=50001
Digite 'PAREAR <host> <porta>' para sincronizar ou 'SAIR' para fechar.
PAREAR localhost 60001
```

### Exemplo de Pareamento:
No console do Servidor A (que está rodando na porta 50001 para parceiros), para puxar dados do Servidor B (que está rodando na porta 60001 para parceiros):

* Comando do Servidor > 'PAREAR' localhost 60001
```bash
java -cp out Main server 0.0.0.0 6000 dataA.psv
```
### Logs de Feedback

O servidor exibe logs para facilitar o acompanhamento das atividades.

### Quando um cliente conecta e joga:
```
Servidor iniciado. Portas: Clientes=50000, Parceiros=50001
[DEBUG] Checando Resposta: Recebido=2, Correto=2
```
Quando um servidor parceiro pede a base de dados:
```bash
(O log é silencioso para não poluir o terminal, mas a conexão é tratada)
```
### Quando este servidor faz PAREAR com outro:
```
Comando do Servidor > PAREAR localhost 60001
Tentando parear com localhost:60001
Pareamento completo! 9 novas questões adicionadas.
Temas atuais: [Futebol, Historia, Ciencia, Conhecimentos Gerais]
```
- FORMATO BANCO DE DADOS
O banco de perguntas local é salvo em um arquivo de texto (.txt ou .md) com um formato simples de "ponto-e-vírgula e vírgula":
tema;texto da pergunta;opcaoA,opcaoB,opcaoC;indice_correto

Exemplo:
```
Historia;Em que ano foi declarada a Independência do Brasil?;1500,1889,1822,1808;2
```
Este formato é lido apenas na inicialização do servidor. O mais importante é que, ao ser carregada, cada pergunta é convertida para um objeto schema.Question. Este objeto possui um ID determinístico, calculado a partir do seu conteúdo. Isso garante que, mesmo que servidores diferentes carreguem a mesma pergunta de arquivos diferentes, ela terá o mesmo ID, evitando duplicatas durante o pareamento.

### Desafios encontrados
Durante o desenvolvimento, alguns desafios técnicos surgiram, servindo como importantes oportunidades de aprendizado.
Uma delas foi a troca de dados entre servidores distintos:
- No caso foi: Como transferir uma lista inteira de objetos Questao de um servidor para outro de forma eficiente e sem a necessidade de bibliotecas externas.
Outro desafio foi Erros na Lógica de Verificação de Respostas:
- Após as refatorações do protocolo, o sistema passou a considerar todas as respostas dos jogadores como incorretas.Foi necessário rastrear o fluxo da resposta do jogador: desde a entrada da letra no cliente, sua conversão para índice numérico, o envio via socket, e a recepção e comparação no servidor.

### Soluções pensadas
Troca de Dados Complexos entre Servidores:
- Foi adotada a serialização de objetos nativa do Java. A classe Questao implementou a interface java.io.Serializable, permitindo que a lista de objetos fosse convertida em um fluxo de bytes, enviada via ObjectOutputStream e reconstruída no destino com ObjectInputStream. Isso manteve o código alinhado aos requisitos e ao paradigma de orientação a objetos.
  
Erros na Lógica de Verificação de Respostas:

- Foi adicionada uma linha de depuração (System.out.println) estratégica no servidor (ConexaoCliente) para imprimir o valor recebido do cliente e o valor esperado. Isso permitiu visualizar a discrepância em tempo real e identificar uma falha na lógica de validação do comando RESPOSTA, que foi prontamente corrigida. A técnica demonstrou a importância de ferramentas simples de log para a depuração de sistemas distribuídos.











