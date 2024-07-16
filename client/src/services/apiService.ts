import axios from 'axios';

const domainApi = 'http://localhost:8080/api';

const apiService = {
  getText: async () => {
    try {
      return await axios.get(`${domainApi}/text/hello`);
    } catch (error) {
      console.error('Error fetching data from foreign server:', error);
      throw error;
    }
  }
}

export default apiService;
