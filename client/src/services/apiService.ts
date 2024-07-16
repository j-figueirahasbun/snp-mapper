import axios from 'axios';

const domainApi = 'http://localhost:8080/api';

const apiService = {
  getText: async () => {
    try {
      const response = await axios.get(`${domainApi}/text`);

      return JSON.stringify(response.data.textToDisplay);
    } catch (error) {
      console.error('Error fetching data from foreign server:', error);
      throw error;
    }
  }
}
