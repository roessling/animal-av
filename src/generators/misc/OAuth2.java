/*
 * OAuth2.java
 * Vincent Riesop und Nils Mäser, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import algoanim.primitives.Text;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import generators.misc.oauth.utils.OAuthEntity;
import generators.misc.oauth.utils.ProtocolLayout;
import generators.misc.oauth.utils.ProtocolArrow;
import generators.misc.oauth.utils.Token;
import interactionsupport.models.HtmlDocumentationModel;


public class OAuth2 implements ValidatingGenerator {

    /**
     * The Animal Language Object.
     */
    public Language lang;

    /**
     * The width of the animal whole Window.
     */
    private int WIDTH = 1200;
    /**
     * The height of the animal whole Window.
     */
    private int HEIGHT = 800;

    /**
     * Internal class that helps to position the Entities, info box and heading.
     */
    ProtocolLayout oauthLayout;

    private int moveEntityTimeInMs, stepSlideInTimeInMs,tokenMoveTimeInMs;

    private TextProperties headingProps,parameterTextProps,smallText;
	private RectProperties tokenRectProps;

    private static final String DESCRIPTION =
              "OAuth2.0 is a set of protocols designed for delegating authorization for protected resources. "
            + "That has the advantage of using data or services from a provider with an external application.\n\n"
            + "Example:\nAccess Facebook services and data via a third party application.";


    public String getName() {
    	return "OAuth2.0 [EN]";
    }

    public String getAlgorithmName() {
        return "OAuth 2.0";
    }

    public String getAnimationAuthor() {
        return "Nils Mäser and Vincent Riesop";
    }

    public String getDescription(){
        return DESCRIPTION;
    }

    public String getCodeExample(){
        return "Abstract Protocol Flow:\n" +
                "Application          => User:                 Authorization Request\n" +
                "User                 => Application:          Authorization Grant\n" +
                "Application          => Authorization Server: Authorization Grant\n"+
                "Authorization Server => Application:          Access Token\n" +
                "Application          => Resource Server:      Access Token\n"+
                "Resource Server      => Application:          Protected Resource";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    
    public void init(){
        lang = new AnimalScript("OAuth2.0 [DE]", "Vincent Riesop und Nils Mäser", WIDTH, HEIGHT);
        lang.setStepMode(true);
            

		tokenRectProps = new RectProperties();
		tokenRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		tokenRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.darkGray);
		tokenRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);


        headingProps = new TextProperties();
        headingProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 18));
        headingProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        smallText = new TextProperties();
        smallText.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.MONOSPACED, Font.PLAIN, 10));
        smallText.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {


        RectProperties infoBoxMarkerProps = (RectProperties)  props.getPropertiesByName("infoBoxBorderRectProp");
        RectProperties entityRectProp = (RectProperties)  props.getPropertiesByName("entityRectProp");

        TextProperties tokenTextProp = (TextProperties)  props.getPropertiesByName("tokenTextProp");
        RectProperties tokenRectProp = (RectProperties)  props.getPropertiesByName("tokenRectProp");
        CircleProperties tokenCircleProp = (CircleProperties)  props.getPropertiesByName("tokenCircleProp");

        parameterTextProps = (TextProperties)  props.getPropertiesByName("parameterText");
        TextProperties infoTextProps = (TextProperties)  props.getPropertiesByName("infoText");


        moveEntityTimeInMs = (int) primitives.get("moveEntityTimeInMs");
        stepSlideInTimeInMs = (int) primitives.get("stepSlideInTimeInMs");
        tokenMoveTimeInMs = (int) primitives.get("tokenMoveTimeInMs");


        int radius = (int) primitives.get("radius");
        int xLayoutCenter = (int) primitives.get("xLayoutCenter");
        int yLayoutCenter = (int) primitives.get("yLayoutCenter");

        Token token;

        // ** Initializing the Layout **
        //520, 400, 350
        oauthLayout = new ProtocolLayout(lang, xLayoutCenter,yLayoutCenter,radius, WIDTH, HEIGHT,infoBoxMarkerProps,infoTextProps, stepSlideInTimeInMs,moveEntityTimeInMs );

        LinkedList<ProtocolArrow> protocolArrows = new LinkedList<ProtocolArrow>();

        // Start the Intro

        oauthLayout.drawIntroOutroLayout();

        oauthLayout.setHeading("OAuth2.0 - Intro");


        oauthLayout.addInfoBoxLine("This Animal animation visualizes the four OAuth2.0 flows. In order to understand the hows and whys of each flow, basic oauth terminology is introduced when necessary.");
        oauthLayout.addInfoBoxLine("However, this is not a copy of the OAuth2.0 Specification. If you need implementation details, please see the latest OAuth2.0 specs.");
        oauthLayout.addInfoBoxLine("");
        oauthLayout.addInfoBoxLine("What is OAuth 2.0?",headingProps);
        oauthLayout.addInfoBoxLine("OAuth2.0 is a set of protocols designed for delegating authorization for protected resources.");
        oauthLayout.addInfoBoxLine("This has the advantage of using data or services from a provider with an external application");
        oauthLayout.addInfoBoxLine("Example. Access Facebook services and data via a third party application.");

        oauthLayout.addInfoBoxLine("OAuth Terminology",headingProps);
        oauthLayout.addInfoBoxLine("Before you can jump to the OAuth2.0 protocol, you should be familiar with the following terms and concepts:");

        oauthLayout.addInfoBoxLine("Protected Resource");
        oauthLayout.addInfoBoxLine("- A resource can be any data or service.");
        oauthLayout.addInfoBoxLine("- It is protected by authentication and authorization enforcement.");

        oauthLayout.addInfoBoxLine("Authentication     ");
        oauthLayout.addInfoBoxLine("- The user proves his identity. This means that the authentication enforcement point knows the identity and how to verify the authentication proof.");

        oauthLayout.addInfoBoxLine("Authorization");
        oauthLayout.addInfoBoxLine("- Actions can be performed on a resource. Data can be read, written or deleted, and a service can be executed for example.");

        oauthLayout.addInfoBoxLine("- Which actions the user is allowed to perform depends on his permissions for that resource.");
        oauthLayout.addInfoBoxLine("- If the user has permission to perform a certain action, he will be authorized to do so if he performs that action on that resource.");
        oauthLayout.addInfoBoxLine("");

        oauthLayout.addInfoBoxLine("How to authorize a third party app? Or, how to delegate authorization?",headingProps);
        oauthLayout.addInfoBoxLine("A third party app provides an access token at every request to that resource. Whether the third party app is authorized to perform the requested action depends on");
        oauthLayout.addInfoBoxLine("whether the user has granted the particular permission for that third party app. This access token contains information about the requesting third party app ");
        oauthLayout.addInfoBoxLine("as well as optional information. The access token can be extended to the needs of the use cases. A common example is login via social media provider. In the use case login,  ");
        oauthLayout.addInfoBoxLine("information about the user may be relevant. Note: OAuth2.0 is not designed for authentication, it is designed for delegating authorization! The resource server is well ");
        oauthLayout.addInfoBoxLine("aware that the requesting party is acting on behalf of the user, so it would be a big no-go to issue an authentication assuring token to an entity that is ");
        oauthLayout.addInfoBoxLine("knowingly NOT the authentic person.");
        oauthLayout.addInfoBoxLine("");

        oauthLayout.addInfoBoxLine("How to get an access token?",headingProps);
        oauthLayout.addInfoBoxLine("How to obtain such an access token is a major part of this Animal Visualization. There are four different flows. Please continue to learn more about these four flows.");

        lang.nextStep("OAuth2.0 - Client Credentials Roles - Client");
        oauthLayout.clearText();
        lang.hideAllPrimitives();
        // End of Intro..


		// ** Client Credentials **
        oauthLayout.drawFlowLayout();


        // * Introducing Roles *
        oauthLayout.setHeading("OAuth2.0 - Client Credentials Roles");


        oauthLayout.addInfoBoxLine("Client",headingProps);
        oauthLayout.addInfoBoxLine("Third party application that is requesting access to a protected ");
        oauthLayout.addInfoBoxLine("resource on behalf of the resource owner. The client must get authorized");
        oauthLayout.addInfoBoxLine("by the resource owner in order to access a protected resource.");


        OAuthEntity client = new OAuthEntity(oauthLayout, "Client",entityRectProp);
        lang.nextStep("OAuth2.0 - Client Credentials Roles - Client");


        oauthLayout.addInfoBoxLine("");
        oauthLayout.addInfoBoxLine("Authorization Server:",headingProps);
        oauthLayout.addInfoBoxLine("This server issues access tokens to authorized and authenticated clients.");
        oauthLayout.addInfoBoxLine("(A client always identifies itself with a client_id.");
        oauthLayout.addInfoBoxLine("A symmetric key is commonly used for client authentication.)");
        oauthLayout.addInfoBoxLine("For simplicity and a better overview, the resource server and the ");
        oauthLayout.addInfoBoxLine("authorization server are often displayed as one entity.");
        oauthLayout.addInfoBoxLine("Combining the authorization and resource server APIs is perfectly ");
        oauthLayout.addInfoBoxLine("acceptable from the developer's point of view");
        oauthLayout.addInfoBoxLine("As an architect for infrastructure, you could separate ");
        oauthLayout.addInfoBoxLine("the authorization and the resource server APIs.");

        OAuthEntity authServer = new OAuthEntity(oauthLayout, "Auth. Server",entityRectProp);

        // ** Setup Flow - Client Credential

        // ... pre next Step setup..
        protocolArrows.add(new ProtocolArrow(oauthLayout, client, authServer, "(A)", "Client Authentication",0));
        protocolArrows.add(new ProtocolArrow(oauthLayout, authServer, client, "(B)", "Access Token",1));
        oauthLayout.registerSteps(protocolArrows);
        lang.nextStep("OAuth2.0 - Client Credentials Roles - Auth. Server");


        // ... post next step setup
        oauthLayout.clearText();
        oauthLayout.setHeading("OAuth2.0 - Client Credentials Flow");


        // ** Client Credentials Flow **
        oauthLayout.clearDetailsOnly();
        oauthLayout.addStepDetailsLine(oauthLayout.getStepWithId(0).getStrLabel(),headingProps);
        oauthLayout.addStepDetailsLine("Client sends a request to get an access token ");
        oauthLayout.addStepDetailsLine("The request contains query parameters:");
        oauthLayout.addStepDetailsLine("grant_type=client_credentials", parameterTextProps);
        oauthLayout.addStepDetailsLine("client_id=<client_id>", parameterTextProps);
        oauthLayout.addStepDetailsLine("client_secret=<client_secret>", parameterTextProps);


        token = new Token(lang, "credentials", tokenTextProp, tokenCircleProp, tokenRectProp, getArrowStart(0),tokenMoveTimeInMs);
        token.moveTo(getArrowEnd(0));
        showArrow(0);
        lang.nextStep("OAuth2.0 - Client Credentials Flow Step 1");
        token.hide();
        oauthLayout.clearDetailsOnly();
        oauthLayout.addStepDetailsLine(oauthLayout.getStepWithId(1).getStrLabel(),headingProps);
        oauthLayout.addStepDetailsLine("The authorization server authenticates the client, and if valid,");
        oauthLayout.addStepDetailsLine("issues an access token.");


        token = new Token(lang, "Access Token", tokenTextProp, tokenCircleProp, tokenRectProp, getArrowStart(1),tokenMoveTimeInMs);
        token.moveTo(getArrowEnd(1));
        showArrow(1);
        // ** End Of Client Credentials Flow.


        //** Setup Authorization Code Grant

        protocolArrows.clear();
        lang.nextStep("OAuth2.0 - Client Credentials Flow Step 2");
        token.hide();;


        // .. post next step setup

        oauthLayout.clearEntities();
        oauthLayout.clearText();


        // ** Introducing Roles for authorization code grant
        oauthLayout.setHeading("OAuth2.0 - Authorization Code Roles");


        oauthLayout.addInfoBoxLine("User Agent",headingProps);
        oauthLayout.addInfoBoxLine(" The client which initiates a request. These are often browsers,");
        oauthLayout.addInfoBoxLine(" editors, spiders (web-traversing robots), or other end user tools.");



        OAuthEntity userAgent = new OAuthEntity(oauthLayout, "User Agent",entityRectProp);
        lang.nextStep("OAuth2.0 - Authorization Code Roles - User Agent");



        oauthLayout.addInfoBoxLine("");
        oauthLayout.addInfoBoxLine("Client",headingProps);
        oauthLayout.addInfoBoxLine("Third party application that is requesting access to a protected ");
        oauthLayout.addInfoBoxLine("resource on behalf of the resource owner. The client must get authorized");
        oauthLayout.addInfoBoxLine("by the resource owner in order to access a protected resource.");


        client = new OAuthEntity(oauthLayout, "Client",entityRectProp);
        lang.nextStep("OAuth2.0 - Authorization Code Roles - Client");


        oauthLayout.addInfoBoxLine("");
        oauthLayout.addInfoBoxLine("Authorization Server:",headingProps);
        oauthLayout.addInfoBoxLine("This server issues access tokens to authorized and authenticated clients.");
        oauthLayout.addInfoBoxLine("(A client always identifies itself with a client_id.");
        oauthLayout.addInfoBoxLine("A symmetric key is commonly used for client authentication.)");
        oauthLayout.addInfoBoxLine("For simplicity and a better overview, the resource server and the ");
        oauthLayout.addInfoBoxLine("authorization server are often displayed as one entity.");
        oauthLayout.addInfoBoxLine("Combining the authorization and resource server APIs is perfectly ");
        oauthLayout.addInfoBoxLine("acceptable from the developer's point of view");
        oauthLayout.addInfoBoxLine("As an architect for infrastructure, you could separate ");
        oauthLayout.addInfoBoxLine("the authorization and the resource server APIs.");

        authServer = new OAuthEntity(oauthLayout, "Auth. Server",entityRectProp);
        lang.nextStep("OAuth2.0 - Authorization Code Roles - Auth. Server");



        oauthLayout.addInfoBoxLine("");
        oauthLayout.addInfoBoxLine("Resource owner",headingProps);
        oauthLayout.addInfoBoxLine("Owns the resource and can grant access to a protected resource.");


        OAuthEntity resourceOwner = new OAuthEntity(oauthLayout, "Res. Owner",entityRectProp);
        lang.nextStep("OAuth2.0 - Authorization Code Roles - Res. Owner");



        //** Setup Authorization Code Grant Flow
        oauthLayout.clearText();
        oauthLayout.setHeading("OAuth2.0 - Authorization Code Flow");

        // ... pre next step setup
        protocolArrows.add(new ProtocolArrow(oauthLayout, client, authServer, "(A)", "Request Authorization",0));
        protocolArrows.add(new ProtocolArrow(oauthLayout, resourceOwner, authServer, "(B)", "User authorizes Request",1));
        protocolArrows.add(new ProtocolArrow(oauthLayout, authServer, userAgent, "(C)", "Authorization Code Grant ",2));
        protocolArrows.add(new ProtocolArrow(oauthLayout, userAgent, client, "(D)", "Authorization Code Grant",3));
        protocolArrows.add(new ProtocolArrow(oauthLayout, client, authServer, "(E)", "Access Token Request",4));
        protocolArrows.add(new ProtocolArrow(oauthLayout, authServer, client, "(F)", "Access Token Grant",5));
        oauthLayout.registerSteps(protocolArrows);
        oauthLayout.clearText();

        lang.nextStep();

        oauthLayout.clearDetailsOnly();
        oauthLayout.addStepDetailsLine(oauthLayout.getStepWithId(0).getStrLabel(),headingProps);
        oauthLayout.addStepDetailsLine("The client initiates the flow and requests access on behalf of the user.");
        oauthLayout.addStepDetailsLine("The Request contains the following parameters:");
        oauthLayout.addStepDetailsLine("client_id= <id of the requesting client>",parameterTextProps);
        oauthLayout.addStepDetailsLine("redirect_uri = <Callback URL>",parameterTextProps);
        oauthLayout.addStepDetailsLine("response_type = code",parameterTextProps);
        oauthLayout.addStepDetailsLine("scope = <granular authorization system>",parameterTextProps);
        oauthLayout.addStepDetailsLine(" ... this enables the user to grant or revoke specific ");
        oauthLayout.addStepDetailsLine(" ... permissions for the application (here referred to as client)");

        oauthLayout.addStepDetailsLine("It is common to provide these parameters as query parameters of the");
        oauthLayout.addStepDetailsLine("requesting API call. Example URL:");
        oauthLayout.addStepDetailsLine("https://<fqdn>/v1/oauth/authorize?",smallText);
        oauthLayout.addStepDetailsLine("response_type=code&client_id=CLIENT_ID&redirect_uri=CALLBACK_URL&scope=read",smallText);

        token = new Token(lang,"request", tokenTextProp, tokenCircleProp, tokenRectProp, getArrowStart(0),tokenMoveTimeInMs);
        token.moveTo(getArrowEnd(0));
        showArrow(0);


        lang.nextStep("OAuth2.0 - Authorization Code Flow Step 1");
        token.hide();
        oauthLayout.clearDetailsOnly();
        oauthLayout.addStepDetailsLine(oauthLayout.getStepWithId(1).getStrLabel(),headingProps);
        oauthLayout.addStepDetailsLine("The user is redirected to the Authorization Server.");
        oauthLayout.addStepDetailsLine("He or she will be prompted to log in, unless the user is already logged in.");
        oauthLayout.addStepDetailsLine("Once the identity of the user has been authenticated,");
        oauthLayout.addStepDetailsLine("he or she; will be asked to provide his or her permission.");
        oauthLayout.addStepDetailsLine("Typically the user sees a description of the scopes and the requesting client");
        oauthLayout.addStepDetailsLine("A common challenge is the translation of the descriptions, ");
        oauthLayout.addStepDetailsLine("and the mapping between the technical scopes and the readable descriptions. ");

        token = new Token(lang,"user input", tokenTextProp, tokenCircleProp, tokenRectProp, getArrowStart(1),tokenMoveTimeInMs);
        token.moveTo(getArrowEnd(1));
        showArrow(1);

        lang.nextStep("OAuth2.0 - Authorization Code Flow Step 2");
        token.hide();
        oauthLayout.clearDetailsOnly();
        oauthLayout.addStepDetailsLine(oauthLayout.getStepWithId(2).getStrLabel(),headingProps);
        oauthLayout.addStepDetailsLine("Given that the user has provided his consent to the requested permissions,");
        oauthLayout.addStepDetailsLine("the authorization server sends an authorization code to the callback URL.");
        oauthLayout.addStepDetailsLine("The callback URL is targeted to the client. The User Agent will be redirected ");
        oauthLayout.addStepDetailsLine("together with the authorization code to that client.");


        showArrow(2);

        token = new Token(lang,"Auth Code", tokenTextProp, tokenCircleProp, tokenRectProp, getArrowStart(2),tokenMoveTimeInMs);
        token.moveTo(getArrowEnd(2));

        lang.nextStep("OAuth2.0 - Authorization Code Flow Step 3");

        token.hide();
        oauthLayout.clearDetailsOnly();
        oauthLayout.addStepDetailsLine(oauthLayout.getStepWithId(3).getStrLabel(),headingProps);
        oauthLayout.addStepDetailsLine("The User Agent is redirected to the client. Redirection is initiated in the previous step.");
        showArrow(3);
        token = new Token(lang, "Auth Code", tokenTextProp, tokenCircleProp, tokenRectProp, getArrowStart(3),tokenMoveTimeInMs);
        token.moveTo(getArrowEnd(3));

        lang.nextStep("OAuth2.0 - Authorization Code Flow Step 4");
        token.hide();
        oauthLayout.clearDetailsOnly();
        oauthLayout.addStepDetailsLine(oauthLayout.getStepWithId(4).getStrLabel(),headingProps);
        oauthLayout.addStepDetailsLine("The Client builds a new API call in order to get an access token. This request contains");
        oauthLayout.addStepDetailsLine("client_id = <id of the client>",parameterTextProps);
        oauthLayout.addStepDetailsLine("client_secret = < only known by the Client>",parameterTextProps);
        oauthLayout.addStepDetailsLine("... The user agent should NOT know the secret. ");
        oauthLayout.addStepDetailsLine("... The user agent is typically in an uncontrolled and ");
        oauthLayout.addStepDetailsLine("... and potentially not trustworthy and insecure environment.");
        oauthLayout.addStepDetailsLine("... A browser, a mobile app or whatever.");
        oauthLayout.addStepDetailsLine("... The client secret could be leaked easier in such an environment.");
        oauthLayout.addStepDetailsLine("... However, the user agent is still relevant,");
        oauthLayout.addStepDetailsLine("... because the client should not be able to perform");
        oauthLayout.addStepDetailsLine("... actions without the authorization of the resource owner.");
        oauthLayout.addStepDetailsLine("code=<AUTHORIZATION_CODE>",parameterTextProps);
        oauthLayout.addStepDetailsLine("redirect_uri=CALLBACK_URL",parameterTextProps);

        showArrow(4);
        token = new Token(lang, "Auth Code", tokenTextProp, tokenCircleProp, tokenRectProp, getArrowStart(4),tokenMoveTimeInMs);
        token.moveTo(getArrowEnd(4));

        lang.nextStep("OAuth2.0 - Authorization Code Flow Step 5");
        token.hide();

        oauthLayout.clearDetailsOnly();
        oauthLayout.addStepDetailsLine(oauthLayout.getStepWithId(5).getStrLabel(),headingProps);
        oauthLayout.addStepDetailsLine("The client now receives the access token. With that access token, the client is now");
        oauthLayout.addStepDetailsLine("able to access the requested resources (based on the requested and permitted scopes)");
        oauthLayout.addStepDetailsLine("OAuth2.0 allows a versatile configuration of the access token.");
        oauthLayout.addStepDetailsLine("It should contain the following parameters");
        oauthLayout.addStepDetailsLine("access_token=<The crypto access token>",parameterTextProps);
        oauthLayout.addStepDetailsLine("token_type=bearer",parameterTextProps);
        oauthLayout.addStepDetailsLine("expires_in=<time to live>",parameterTextProps);
        oauthLayout.addStepDetailsLine("refresh_token=<optional crypto token to get a fresh access token.>",parameterTextProps);
        oauthLayout.addStepDetailsLine("scope=<the requested scopes.. e.g. read>",parameterTextProps);
        oauthLayout.addStepDetailsLine("info=<e.g. name and email of the user>",parameterTextProps);
        oauthLayout.addStepDetailsLine("... the info section already contains some basic information. ");
        oauthLayout.addStepDetailsLine("... for more information, the client should request the /userinfo API.");

        oauthLayout.addStepDetailsLine("");




        showArrow(5);
        token = new Token(lang,"Access Token", tokenTextProp, tokenCircleProp, tokenRectProp, getArrowStart(5),tokenMoveTimeInMs);
        token.moveTo(getArrowEnd(5));

        // ... post next step setup

        // ** End of implicit code flow
        protocolArrows.clear();
        lang.nextStep("OAuth2.0 - Authorization Code Flow Step 6");
        token.hide();

        lang.hideAllPrimitives();
        oauthLayout.clearEntities();
        oauthLayout.clearText();

        // ** Introducing Roles for authorization code grant
        oauthLayout.setHeading("OAuth2.0 - Implicit Roles");

        oauthLayout.addInfoBoxLine("Client",headingProps);
        oauthLayout.addInfoBoxLine("Third party application that is requesting access to a protected ");
        oauthLayout.addInfoBoxLine("resource on behalf of the resource owner. The client must get authorized");
        oauthLayout.addInfoBoxLine("by the resource owner in order to access a protected resource.");


        client = new OAuthEntity(oauthLayout, "Client",entityRectProp);
        lang.nextStep("OAuth2.0 - Implicit Roles - Client");


        oauthLayout.addInfoBoxLine("User Agent",headingProps);
        oauthLayout.addInfoBoxLine(" The client which initiates a request. These are often browsers,");
        oauthLayout.addInfoBoxLine(" editors, spiders (web-traversing robots), or other end user tools.");

        userAgent = new OAuthEntity(oauthLayout, "User Agent",entityRectProp);
        lang.nextStep("OAuth2.0 - Implicit Roles - User Agent");


        oauthLayout.addInfoBoxLine("");
        oauthLayout.addInfoBoxLine("Authorization Server:",headingProps);
        oauthLayout.addInfoBoxLine("This server issues access tokens to authorized and authenticated clients.");
        oauthLayout.addInfoBoxLine("(A client always identifies itself with a client_id.");
        oauthLayout.addInfoBoxLine("A symmetric key is commonly used for client authentication.)");
        oauthLayout.addInfoBoxLine("For simplicity and a better overview, the resource server and the ");
        oauthLayout.addInfoBoxLine("authorization server are often displayed as one entity.");
        oauthLayout.addInfoBoxLine("Combining the authorization and resource server APIs is perfectly ");
        oauthLayout.addInfoBoxLine("acceptable from the developer's point of view");
        oauthLayout.addInfoBoxLine("As an architect for infrastructure, you could separate ");
        oauthLayout.addInfoBoxLine("the authorization and the resource server APIs.");

        authServer = new OAuthEntity(oauthLayout, "Auth. Server",entityRectProp);
        lang.nextStep("OAuth2.0 - Implicit Roles - Auth. Server");



        oauthLayout.addInfoBoxLine("");
        oauthLayout.addInfoBoxLine("Resource owner",headingProps);
        oauthLayout.addInfoBoxLine("Owns the resource and can grant access to a protected resource.");


        resourceOwner = new OAuthEntity(oauthLayout, "Res. Owner",entityRectProp);


        // ... pre next step setup
        protocolArrows.clear();
        protocolArrows.add(new ProtocolArrow(oauthLayout, client, authServer, "(A)", "Authorization Request",0));
        protocolArrows.add(new ProtocolArrow(oauthLayout, resourceOwner, authServer, "(B)", "User authorizes Request",1));
        protocolArrows.add(new ProtocolArrow(oauthLayout, authServer, userAgent, "(C)", "Redirect URI with Fragment",2));
        protocolArrows.add(new ProtocolArrow(oauthLayout, userAgent, client, "(D)", "Follow Redirect URI",3));
        protocolArrows.add(new ProtocolArrow(oauthLayout, client, userAgent, "(E)", "send Token extract script",4));
        protocolArrows.add(new ProtocolArrow(oauthLayout, userAgent, client, "(F)", "Pass Token to Client ",5));
        oauthLayout.registerSteps(protocolArrows);


        oauthLayout.setHeading("OAuth2.0 - Implicit Code Flow");
        oauthLayout.clearText();
        lang.nextStep("OAuth2.0 - Implicit Roles - Res. Owner");

        oauthLayout.clearDetailsOnly();
        oauthLayout.addStepDetailsLine(oauthLayout.getStepWithId(0).getStrLabel(),headingProps);
        oauthLayout.addStepDetailsLine("The client initiates the flow and requests access on behalf of the user.");
        oauthLayout.addStepDetailsLine("The request contains the following parameters:");
        oauthLayout.addStepDetailsLine("client_id= <id of the requesting client>",parameterTextProps);
        oauthLayout.addStepDetailsLine("redirect_uri = <Callback URL>",parameterTextProps);
        oauthLayout.addStepDetailsLine("response_type = token",parameterTextProps);
        oauthLayout.addStepDetailsLine(" ... This means that the client receives the access token without the auth. code.");

        oauthLayout.addStepDetailsLine("scope = <granular authorization system)>",parameterTextProps);
        oauthLayout.addStepDetailsLine(" ... this enables the user to grant or revoke specific ");
        oauthLayout.addStepDetailsLine(" ... permissions for the application (here referred to as client)");

        oauthLayout.addStepDetailsLine("It is common to provide these parameters as query Parameters ");
        oauthLayout.addStepDetailsLine("of the requesting API call. Example URL:");
        oauthLayout.addStepDetailsLine("https://<fqdn>/v1/oauth/authorize?",smallText);
        oauthLayout.addStepDetailsLine("response_type=token&client_id=CLIENT_ID&redirect_uri=CALLBACK_URL&scope=read",smallText);

        token = new Token(lang,"request", tokenTextProp, tokenCircleProp, tokenRectProp, getArrowStart(0),tokenMoveTimeInMs);
        token.moveTo(getArrowEnd(0));
        showArrow(0);
        lang.nextStep("OAuth2.0 - Implicit Flow Step 1");
        token.hide();
        oauthLayout.clearDetailsOnly();
        oauthLayout.addStepDetailsLine(oauthLayout.getStepWithId(1).getStrLabel(),headingProps);
        oauthLayout.addStepDetailsLine("The user is redirected to the Authorization Server.");
        oauthLayout.addStepDetailsLine("He or She will be prompted to login, unless the user is already logged in.");
        oauthLayout.addStepDetailsLine("Once the identity of the user has been authenticated,");
        oauthLayout.addStepDetailsLine("he or she; will be asked to provide his or her permission.");
        oauthLayout.addStepDetailsLine("Typically the user sees a description of the scopes and the requesting client");
        oauthLayout.addStepDetailsLine("A common challenge is the translation of the descriptions, ");
        oauthLayout.addStepDetailsLine("and the mapping between the technical scopes and the readable descriptions. ");

        token = new Token(lang,"user input", tokenTextProp, tokenCircleProp, tokenRectProp, getArrowStart(1),tokenMoveTimeInMs);
        token.moveTo(getArrowEnd(1));
        showArrow(1);
        lang.nextStep("OAuth2.0 - Implicit Flow Step 2");
        token.hide();
        oauthLayout.clearDetailsOnly();
        oauthLayout.addStepDetailsLine(oauthLayout.getStepWithId(2).getStrLabel(),headingProps);
        oauthLayout.addStepDetailsLine("If the user authorizes the request, the user agent gets redirected to the client");
        oauthLayout.addStepDetailsLine("Redirect URI. The access token is part of the URI (URI Fragment)");
        oauthLayout.addStepDetailsLine("Example URI: https://animal.rocks/callback#token=ACCESS_TOKEN");

        token = new Token(lang, "URI+Fragment", tokenTextProp, tokenCircleProp, tokenRectProp, getArrowStart(2),tokenMoveTimeInMs);
        token.moveTo(getArrowEnd(2));
        showArrow(2);
        lang.nextStep("OAuth2.0 - Implicit Flow Step 3");
        token.hide();

        oauthLayout.clearDetailsOnly();
        oauthLayout.addStepDetailsLine(oauthLayout.getStepWithId(3).getStrLabel(),headingProps);
        oauthLayout.addStepDetailsLine("The user Agent follows the redirection to the Client, but retains the full URI.");

        token = new Token(lang, "URI+Fragment", tokenTextProp, tokenCircleProp, tokenRectProp, getArrowStart(3),tokenMoveTimeInMs);
        token.moveTo(getArrowEnd(3));
        showArrow(3);
        lang.nextStep("OAuth2.0 - Implicit Flow Step 4");
        token.hide();

        oauthLayout.clearDetailsOnly();
        oauthLayout.addStepDetailsLine(oauthLayout.getStepWithId(4).getStrLabel(),headingProps);
        oauthLayout.addStepDetailsLine("The client returns a webpage containing a script.");
        oauthLayout.addStepDetailsLine("This script can extract the access token ");
        oauthLayout.addStepDetailsLine("from the full redirect URI the user agent has retained.");

        token = new Token(lang, "script", tokenTextProp, tokenCircleProp, tokenRectProp, getArrowStart(4),tokenMoveTimeInMs);
        token.moveTo(getArrowEnd(4));
        showArrow(4);

        lang.nextStep("OAuth2.0 - Implicit Flow Step 5");
        token.hide();
        oauthLayout.clearDetailsOnly();
        oauthLayout.addStepDetailsLine(oauthLayout.getStepWithId(5).getStrLabel(),headingProps);
        oauthLayout.addStepDetailsLine("The user agent executes the script, gets the access token and finally sends ");
        oauthLayout.addStepDetailsLine("the access token to the client.");

        token = new Token(lang, "access token", tokenTextProp, tokenCircleProp, tokenRectProp, getArrowStart(5),tokenMoveTimeInMs);
        token.moveTo(getArrowEnd(5));
        showArrow(5);


        // ** End of implicit flow
        protocolArrows.clear();
        lang.nextStep("OAuth2.0 - Implicit Flow Step 6");
        token.hide();

        lang.hideAllPrimitives();
        oauthLayout.clearEntities();
        oauthLayout.clearText();

        // ** Resource Owner Password Credentials Roles


        oauthLayout.setHeading("OAuth2.0 - Resource Owner Password Credentials Roles");

        oauthLayout.addInfoBoxLine("");
        oauthLayout.addInfoBoxLine("Client",headingProps);
        oauthLayout.addInfoBoxLine("Third party application that is requesting access to a protected ");
        oauthLayout.addInfoBoxLine("resource on behalf of the resource owner. The client must get authorized");
        oauthLayout.addInfoBoxLine("by the resource owner in order to access a protected resource.");


        client = new OAuthEntity(oauthLayout, "Client",entityRectProp);
        lang.nextStep("OAuth2.0 - Resource Owner Password Credentials Roles - Client");


        oauthLayout.addInfoBoxLine("");
        oauthLayout.addInfoBoxLine("Resource owner",headingProps);
        oauthLayout.addInfoBoxLine("Owns the resource and can grant access to a protected resource.");


        resourceOwner = new OAuthEntity(oauthLayout, "Res. Owner",entityRectProp);
        lang.nextStep("OAuth2.0 - Resource Owner Password Credentials Roles - Res. Owner");

        oauthLayout.addInfoBoxLine("");
        oauthLayout.addInfoBoxLine("Authorization Server:",headingProps);
        oauthLayout.addInfoBoxLine("This server issues access tokens to authorized and authenticated clients.");
        oauthLayout.addInfoBoxLine("(A client always identifies itself with a client_id.");
        oauthLayout.addInfoBoxLine("A symmetric key is commonly used for client authentication.)");
        oauthLayout.addInfoBoxLine("For simplicity and a better overview, the resource server and the ");
        oauthLayout.addInfoBoxLine("authorization server are often displayed as one entity.");
        oauthLayout.addInfoBoxLine("Combining the authorization and resource server APIs is perfectly ");
        oauthLayout.addInfoBoxLine("acceptable from the developer's point of view");
        oauthLayout.addInfoBoxLine("As an architect for infrastructure, you could separate ");
        oauthLayout.addInfoBoxLine("the authorization and the resource server APIs.");

        authServer = new OAuthEntity(oauthLayout, "Auth. Server",entityRectProp);
        lang.nextStep("OAuth2.0 - Resource Owner Password Credentials Roles - Auth. Server");


        // ** Resource Owner Password Credentials Flow

        // ... pre next step setup
        protocolArrows.clear();
        protocolArrows.add(new ProtocolArrow(oauthLayout, resourceOwner, client, "(A)", "Password Credentials",0));
        protocolArrows.add(new ProtocolArrow(oauthLayout, client, authServer, "(B)", "Password Credentials",1));
        protocolArrows.add(new ProtocolArrow(oauthLayout, authServer, client, "(C)", "Access Token (Token)",2));
        oauthLayout.registerSteps(protocolArrows);

        oauthLayout.setHeading("OAuth2.0 - Resource Owner Password Credentials Flow");
        oauthLayout.clearText();

        lang.nextStep();

        oauthLayout.clearDetailsOnly();
        oauthLayout.addStepDetailsLine(oauthLayout.getStepWithId(0).getStrLabel(),headingProps);
        oauthLayout.addStepDetailsLine("The User sends his credentials (password and user name) directly to the client.");

        token = new Token(lang, "credentials", tokenTextProp, tokenCircleProp, tokenRectProp, getArrowStart(0),tokenMoveTimeInMs);
        token.moveTo(getArrowEnd(0));
        showArrow(0);

        lang.nextStep("OAuth2.0 - Resource Owner Password Credentials Flow Step 1");
        token.hide();
        oauthLayout.clearDetailsOnly();
        oauthLayout.addStepDetailsLine(oauthLayout.getStepWithId(1).getStrLabel(),headingProps);
        oauthLayout.addStepDetailsLine("The client sends a request to the authorization server's /token endpoint");
        oauthLayout.addStepDetailsLine("grant_type=password",parameterTextProps);
        oauthLayout.addStepDetailsLine("username=<user name>",parameterTextProps);
        oauthLayout.addStepDetailsLine("password=<password>",parameterTextProps);
        oauthLayout.addStepDetailsLine("client_id=<client_id>",parameterTextProps);

        token = new Token(lang, "request", tokenTextProp, tokenCircleProp, tokenRectProp, getArrowStart(1),tokenMoveTimeInMs);
        token.moveTo(getArrowEnd(1));
        showArrow(1);
        lang.nextStep("OAuth2.0 - Resource Owner Password Credentials Flow Step 2");
        token.hide();
        oauthLayout.clearDetailsOnly();
        oauthLayout.addStepDetailsLine(oauthLayout.getStepWithId(2).getStrLabel(),headingProps);
        oauthLayout.addStepDetailsLine("The authorization server returns the access token.");
        oauthLayout.addStepDetailsLine("The client is now authorized.");

        token = new Token(lang, "access token", tokenTextProp, tokenCircleProp, tokenRectProp, getArrowStart(2),tokenMoveTimeInMs);
        token.moveTo(getArrowEnd(2));
        showArrow(2);

        lang.nextStep("OAuth2.0 - Resource Owner Password Credentials Flow Step 3");
        oauthLayout.clearText();
        oauthLayout.clearEntities();
        lang.hideAllPrimitives();

        // ** Outro Page

        oauthLayout.drawIntroOutroLayout();

        oauthLayout.setHeading("OAuth2.0 - Outro");

        oauthLayout.addInfoBoxLine("You have learned",headingProps);
        oauthLayout.addInfoBoxLine("How a client can obtain an access token via client credentials flow, authorization code flow, implicit flow and Resource owner password flow");
        oauthLayout.addInfoBoxLine("Which types of entities are involved in each Flow");
        oauthLayout.addInfoBoxLine("Trust is established between a Client and the authorization Server. (Hint: its the client_secret, a symmetric key)");
        oauthLayout.addInfoBoxLine("OAuth is designed for delegating authorization to third party apps.");
        oauthLayout.addInfoBoxLine("");

        oauthLayout.addInfoBoxLine("OK, cool.. but how can I implement OAuth?",headingProps);
        oauthLayout.addInfoBoxLine("As you have learned, there are many participating entities. You need a clear picture of the architecture and the use cases before you implement Oauth 2.0.");
        oauthLayout.addInfoBoxLine("The authorization Server requires 3 API End points.");
        oauthLayout.addInfoBoxLine("/authorize end point which accepts new authorization requests from clients",parameterTextProps);
        oauthLayout.addInfoBoxLine("/token end point which issues access tokens",parameterTextProps);
        oauthLayout.addInfoBoxLine("/userinfo optional end point and provides more information of the user if a valid access token is provided in the request",parameterTextProps);
        oauthLayout.addInfoBoxLine("");
        oauthLayout.addInfoBoxLine("The Client must have a valid client_id and client_secret. The client_id and the client_secret is issued by the authorization server.");
        oauthLayout.addInfoBoxLine("A developer portal where client developers can register and request a client_id and client_secret, is helpful. ");
        oauthLayout.addInfoBoxLine( "Otherwise the client_id and the client_secret must be manually exchanged for each client.");
        oauthLayout.addInfoBoxLine("");
        oauthLayout.addInfoBoxLine("The authorization Server should have translations of the scope descriptions.");
        oauthLayout.addInfoBoxLine(".. Yes, you get it now. There are many topics regarding a general architecture. As a client developer you probably don't care about the scope translation.");
        oauthLayout.addInfoBoxLine(".. So, a software architect who designs the end-to-end flow is required. As a responsible software architect, you will design the APIs and the flows.");
        oauthLayout.addInfoBoxLine(".. If your architecture is OAuth2.0 compliant, client developers reading your documentation can use existing OAuth2.0 libraries and configure their clients ");
        oauthLayout.addInfoBoxLine(".. to participate in the flow in order to get an access token.");
        oauthLayout.addInfoBoxLine("");

        oauthLayout.addInfoBoxLine("Further Topics",headingProps);
        oauthLayout.addInfoBoxLine("- Obtaining a refresh token");
        oauthLayout.addInfoBoxLine("- Revoking an access token");
        oauthLayout.addInfoBoxLine("");
        oauthLayout.addInfoBoxLine("The OAuth specification will be opened next. You can close the Animation now, or continue to the specification.");
        oauthLayout.addInfoBoxLine("Link: https://tools.ietf.org/html/rfc6749   (just in case you prefer your own Browser)");


        lang.nextStep("OAuth2.0 - Outro");


        lang.addDocumentationLink(new HtmlDocumentationModel("oauthstandard","https://tools.ietf.org/html/rfc6749"));
        lang.nextStep("OAuth2.0 - Specs");

        lang.finalizeGeneration();

        return lang.toString();
    }

    /**
     *
     * @param id the id of the arrow to show. Arrows are stored in a hashmap where the key is the id.
     */
    private void showArrow(int id){
        for(ProtocolArrow arrow : oauthLayout.protocolArrows)
            if(arrow.id == id) {
                arrow.show();
                oauthLayout.stepsList.slideInItem(id, stepSlideInTimeInMs);
            }
    }

    private Coordinates getArrowStart(int id){
        for(ProtocolArrow arrow : oauthLayout.protocolArrows)
            if(arrow.id == id) {
                return arrow.start;
            }
        return null;
    }
    private Coordinates getArrowEnd(int id){
        for(ProtocolArrow arrow : oauthLayout.protocolArrows)
            if(arrow.id == id) {
                return arrow.end;
            }
        return null;
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> hashtable) throws IllegalArgumentException {


        int val_radius = (int) hashtable.get("radius");
        int val_xLayoutCenter = (int) hashtable.get("xLayoutCenter");
        int val_yLayoutCenter = (int) hashtable.get("yLayoutCenter");

        int val_moveEntityTimeInMs = (int) hashtable.get("moveEntityTimeInMs");
        int val_stepSlideInTimeInMs = (int) hashtable.get("stepSlideInTimeInMs");
        int val_tokenMoveTimeInMs = (int) hashtable.get("tokenMoveTimeInMs");


        if(val_radius < 250)
            return false;
        if(val_moveEntityTimeInMs < 0 )
            return false;
        if(val_stepSlideInTimeInMs < 0 )
            return false;
        if(val_tokenMoveTimeInMs < 0)
            return false;
        if(val_xLayoutCenter < 0)
            return false;
        if(val_yLayoutCenter < 0)
            return false;

        return true;
    }
}