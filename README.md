Eu criei esse repositório pois demorei um pouco para entender OAuth2.0 e OpenID, então após pesquisar bastante, assistir a videos e ler materiais na internet, achei uma boa ideia compartilhar tudo isso aqui de forma
centralizada, talvez possa te ajudar a entender também.

O código Java desse repositório é totalmente baseado na ótima explicação que a [Giuliana Bezerra](https://www.youtube.com/@giulianabezerra) deu [nesse vídeo](https://youtu.be/EQ5EwIYsgIE?si=SWTxovz2aGAUB-TF).
Além disso, caso sinta necessidade, você também deveria assistir ao vídeo totalmente teórico, também da Giuliana, dedicado totalmente ao protocolo OAuth 2.0 e OpenID sem utilizar código:
[O que você deveria saber sobre o Oauth 2.0 e OpenID](https://www.youtube.com/watch?v=68azMcqPpyo)

O texto abaixo é uma tradução, não completamente fiel, feita por mim de [An Illustrated Guide to OAuth and OpenID Connect](https://developer.okta.com/blog/2019/10/21/illustrated-guide-to-oauth-and-oidc)
criado por [David Neal](https://github.com/reverentgeek). Recomendo demais que você dê uma olhada no original, pois é explicado usando desenhos, o que deixa tudo mais simples, além da sua incrível didática.

# OAuth 2.0 e OpenID

**OAuth2.0** é um protocolo (conjunto de regras) onde você permite um sistema que você já possui cadastro (Google, Facebook, Github), compartilhar as suas informações com outra aplicação. Essa prática é conhecida por 
*autorização*. Você autoriza uma aplicação a acessar determinados dados ou usar recursos em seu nome, tudo isso sem fornecer a sua senha

Vamos a um exemplo: Suponha que você encontrou um site super interessante e acha que é uma boa ideia compartilhar ele com todos os seus contatos do e-mail. Porém, imagina ter que selecionar cada contato da
sua lista de contatos? vai dar muito trabalho, e você quer evitar tudo isso. Uma boa notícia é que esse site-super-interessante possui uma funcionalidade que você pode conceder acesso à sua lista de contatos
e ele fará todo o trabalho. O OAuth vai te ajudar nessa.

**1º** - Dentro do site, ele te permite selecionar o e-mail que você quer compartilhar a lista de contatos (Gmail, Yahoo, Outlook, etc).

**2º** - Após clicar no provedor de e-mail de sua preferência, caso você não esteja logado, você será redirecionado à página de login desse e-mail. Ao se logar ou caso já esteja logado,
será redirecionado para a página de autorização. Nessa página você será perguntado se deseja compartilhar sua lista de contatos com o site-super-interessante.

**3º** Caso tenha aceitado compartilhar a lista de contatos, você será redirecionado de volta para o site-super-interessante, provavelmente com uma mensagem de sucesso.

> [!NOTE]
> Não se preocupe, caso você mude de ideia, as aplicações que usam OAuth também possuem uma forma de retirar a permissão. Ou seja, se em algum momento você não quiser mais compartilhar a sua lista de contatos
com o site-super-interessante, você pode ir no seu e-mail e remover a permissão.


## O fluxo OAuth
Acabamos de ver o passo a passo de como funciona o OAuth. Esse passo a passo é composto por etapas visiveis para realizar a autorização e também por algumas etapas invisíveis, onde os dois serviços concordam sobre
alguma forma segura para trocar informações. Esse passo a passo mostra o fluxo mais comum do OAuth 2.0, conhecido como "authorization code"

Antes de continuar, vamos ver algumas palavras comuns do OAuth:
  
- **Resource Owner** - Esse é você. Você é o dono da sua identidade, seus dados e qualquer ação que pode ser tomada usando as suas contas.

- **Client** - A aplicação (o site-super-interessante, por exemplo) que quer acessar algumas das suas informações ou realizar ações em nome do usuário (**Resource Owner**).

- **Authorization Server** - É a aplicação que já conhece o **Resource Owner** pois ele já possui uma conta cadastrada (Gmail, Facebook, Github, por exemplo)

- **Resource Server** - É o responsável pelos recursos que o **Client** quer usar.

- **Redirect URI** - A URL que o **Authorization Server** irá redirecionar o **Resource Owner** de volta após conceder permissão ao **Client**. Esse **Redirect URI** normalmente é chamado de “Callback URL”.

- **Response Type** - O tipo de informação que o **Client** está esperando receber. O tipo mais comum de informação é um código, o **Authorization Code**.

- **Authorization Code** - Um código que possui um curto tempo de vida e que o **Client** fornece para o **Authorization Server** para que seja trocado por um **Acess Token**.

- **Access Token** - É a chave que o **Client** vai usar para se comunicar com o **Resource Server**. É tipo um crachá que vai dar a permissão para que o **Client** possa solicitar os dados para o **Resource Server**
ou realizar ações em nome do **Resource Owner**.

- **Scope** - São as permissões que o **Client** está solicitando. Por exemplo: ter a sua lista de contatos, criar novos contatos, acessar o seu nome de usuário, e-mail, etc.

- **Consent** - O **Authorization Server** pega os **Scopes** que o Client está solicitando e mostra para o **Resource Owner** para que ele decida se quer ou não permitir que o **Client** os acesse.

- **Client ID** - É o código ID que é usado para que o **Client** possa se identificar com o **Authorization Server**

- **Client Secret** - É uma senha que apenas o **Client** e o **Authorization Server** sabem. Isso permite que eles, secretamente e de forma segura, compartilhem informações “por baixo dos panos”.

> [!NOTE]
>  Algumas vezes o **Authorization Server** e o **Resouce Server** são os mesmos serviços. Porém, há alguns casos em que eles não serão o mesmo serviço ou nem mesmo são da mesma empresa.
Um **Authorization Server** pode vir a ser um serviço de terceiros que o **Resource Server** confia.

Agora que conhecemos o vocabulário do OAuth2.0, podemos rever o passo a passo de uma forma mais detalhada:

**1º** Você, o **Resource Owner**, deseja permitir que o site-super-interessante, também conhecido como **Client**, acesse a sua lista de contatos para que ele possa compartilhar o site com seus amigos.

**2º** O **Client** redireciona o seu navegador para o **Authorization Server**, carregando junto à solicitação o **Client ID**, **Redirect URI**, **Response Type** e um ou mais **Scope** que ele quer.

**3º** O **Authorization Server** verifica quem você é, e se necessário, solicita um login.

**4º** O **Authorization Server** te mostra o **Consent**, onde estará os **Scopes** solicitados pelo **Client**. Você dá permissão (ou não).

**5º** O **Authorization Server** redireciona de volta para o **Client** usando o **Redirect URI** junto com um **Authorization Code**.

**6º** O **Client** se comunica com o **Authorization Server** diretamente, (ele não usa o navegador para isso) e de forma segura envia o seu **Client ID**, **Client Secret** e o **Authorization Code**.

**7º** O **Authorization Server** verifica as informações e responde com um **Access Token**.

**8º** Agora o **Client** pode usar o **Access Token** para solicitar ao **Resouce Server** os seus contatos da lista.

## Client ID e Client Secret

Muito antes de você dar permissão para o site-super-interessante acessar os seus contatos, o Client e o Authorization Server já tinham uma relação entre eles. Para isso, o Authorization Server gerou
um Client ID e um Client Secret (também podem ser chamados de App ID e App Secret) e os forneceu para que o Client realizasse autorizações OAuth sempre que necessário.

Como o nome sugere, o Client Secret deve ser mantido em segredo. Apenas o Client e o Authorization Server devem ter acesso. É com ele que o Authorization Server irá verificar que o Client é quem diz ser.

## Ainda não acabou… Agora vamos conhecer o OpenID Connect

O OAuth 2.0 é destinado apenas para autorização, garantindo que uma aplicação tenha acesso a dados e recursos de outra aplicação. O OpenID Connect (OIDC) é uma extensão do OAuth2.0 que adiciona
informações de login e do perfil da pessoa que está logada. A ação de realizar um login, costuma ser chamada de autenticação e as informações do perfil da pessoa que está logada (**Resource Owner**)
é chamada identidade. Quando um Authorization Server oferece suporte OIDC, isso é chamado de identity provider, pois fornece informações sobre o **Resource Owner** para o **Client**. Essas informações 
podem ser transmitidas através de token JWT (JSON Web Token), por exemplo.

O OpenID Connect permite cenários onde um único login pode ser usado entre diversas aplicações, também conhecidas como single sign-on (SSO). Uma aplicação pode oferecer suporte SSO para diferentes
serviços de redes sociais como por exemplo Facebook ou Twitter, fazendo com que os usuários possam optar em re-utilizar essa conta que eles já possuem.

O passo a passo do OpenID é bem parecido com o do OAuth. As únicas diferenças são que no primeiro passo, o openid é pedido no Scope e que no último passo, o **Client** recebe tanto o **Access Token** quanto
o **ID Token** (JWT, por exemplo).

Assim como no passo a passo do OAuth, o **Access Token** do OpenID Connect é um valor que o **Client** não entende. Para o **Client**, o **Access Token** é apenas uma sopa de letrinhas no formato de String usado
para solicitar informações para o **Resource Server**, que por sua vez entende o **Access Token** e sabe se é válido ou não. No entanto, **ID Token** é bem diferente.

## Entenda que: Um ID Token é um JWT

Um **ID Token** é uma String formatada conhecida como JSON Web Token (JWT). Em inglês, JWT é pronunciado como “jots”. O JWT vai parecer como uma sopa de letrinhas pra mim e pra você, porém o **Client**
consegue entendê-lo e extrair as informações que ele carrega, como por exemplo o seu ID, nome, quando você se logou, a expiração do ID Token e se alguma coisa tentou adulterá-lo.
Os dados dentro do **ID Token** são chamados de claims.


