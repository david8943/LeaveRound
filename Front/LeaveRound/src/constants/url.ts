const API_VERSION: string = '/api';

export const API = {
  member: {
    signup: `${API_VERSION}/users`,
    login: `${API_VERSION}/users/login`,
    info: (userId: string) => `${API_VERSION}/users/${userId}`,
    account: (userId: string) => `${API_VERSION}/users/${userId}/acounts`,
  },
  event: {
    getGoldDandelion: (dandelionId: string) => `${API_VERSION}/dandelions/collections/gold/${dandelionId}`,
    getWhiteDandelion: (dandelionId: string) => `${API_VERSION}/dandelions/collections/personal/${dandelionId}`,
    whiteLocation: `${API_VERSION}/daldelions/locations/personal`,
    goldLocation: `${API_VERSION}/dandelions/locations/gold `,
    donateDandelion: `${API_VERSION}/dandelions/donations/organizations`,
    whiteDandelionRanking: `${API_VERSION}/dandelions/donations/rankings/weekly`,
    goldDandelionRanking: `${API_VERSION}/dandelions/donations/rankings/gold/monthly`,
  },
  autoDonation: {
    detail: (autoDonationId: string) => `${API_VERSION}/auto-donations/${autoDonationId}`,
  },
  organization: {
    list: `${API_VERSION}/organizations`,
    project: (organizationId: string) => `${API_VERSION}/organizations/${organizationId}/project`,
    certification: (certificateId: string) => `${API_VERSION}/organizations/${certificateId}`,
  },
};
