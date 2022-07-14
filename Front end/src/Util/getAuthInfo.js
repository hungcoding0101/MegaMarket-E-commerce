import store from '../store'

const authInfo = {
    getAuthInfo: function () {
      const state = store.getState();
      const securityInfo = state.rememberMe === true
                          ? state.signInResult_permanent
                          : state.signInResult;

      return securityInfo;
    },
};

export default authInfo
