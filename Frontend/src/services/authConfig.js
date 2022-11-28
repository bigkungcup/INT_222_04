import { UserAgentApplication } from "msal"

//Configuration object to be passed to MSAL instance on creation. 
const msalConfig = {
    auth: {
        clientId: "90842470-8277-47a8-86fd-4fe694176219",
        authority: "https://login.microsoftonline.com/6f4432dc-20d2-441d-b1db-ac3380ba633d",
        redirectUri: "https://intproj21.sit.kmutt.ac.th/kw4/#/",
    },
    cache: {
        cacheLocation: "localStorage", // This configures where your cache will be stored
        storeAuthStateInCookie: false, // Set this to "true" if you are having issues on IE11 or Edge
    },
};

// Add here the endpoints and scopes for the web API you would like to use.
const apiConfig = {
    uri: "http://localhost:5000/api", // e.g. http://localhost:5000/api
    scopes: ["api://90842470-8277-47a8-86fd-4fe694176219/access_as_user"] // e.g. ["scp1", "scp2"]
};

/**
 * Scopes you add here will be prompted for user consent during sign-in.
 * By default, MSAL.js will add OIDC scopes (openid, profile, email) to any login request.
**/
export const loginRequest = {
    scopes: ["openid", "profile"]
};


//Scopes you add here will be used to request a token from Azure AD to be used for accessing a protected resource.
const tokenRequest = {
    scopes: [...apiConfig.scopes],
};

export const myMSALObj = new UserAgentApplication(msalConfig);
