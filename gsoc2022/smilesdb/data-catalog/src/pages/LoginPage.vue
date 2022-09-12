<template>
  <div>
    <NavBar/>
    <div class="container-sm container-lg">
      <h1>
        Welcome to SEAGrid Data Catalog
      </h1>
      <p>
        SEAGrid Data Catalog provides a sleek web interface for you to browse and search through your SEAGrid data.
        Currently the system can index outputs of several computational chemistry applications including Gaussian,
        Gamess, Molpro and NWChem. Also it allows to publish your data into research data publishing systems, do
        browser based visualization of molecular structure and properties and to run complex search queries to filter
        the data. So now you don't need to download all the data into your local machine after running a HPC
        application but select only the interesting data based on the results of configured post processing steps in the
        system.
      </p>
      <div class="d-grid gap-2 d-md-flex justify-content-md-end">
        <button class="btn btn-primary me-md-3" type="button" v-on:click="loadAuthURL">Login to Explore</button>
      </div>
      <p id="btn-caption">Redirects to CILogon Auth page</p>
    </div>
  </div>

</template>

<script>
import custorStore from "airavata-custos-portal/src/lib/store";
import NavBar from "@/components/NavBar";

export default {
  name: "LoginPage",
  components: {NavBar},
  store: custorStore,
  data() {
    return {
      processingLogin: false,
      errors: []
    }
  },

  methods: {
    async loadAuthURL() {
      this.errors = [];
      this.processingLogin = true;

      try {
        await this.$store.dispatch("auth/fetchAuthorizationEndpoint")
      } catch (e) {
        this.erros.push({
          variant: "danger",
          title: "Network Error",
          description: "Please contact the system administrator",
          source: e
        });
      }
      this.processingLogin = false
    },
  }
}
</script>

<style scoped>

.container-lg {
  margin-top: 5%;
  background-color: #f8f9fa;
  border-radius: 12px 12px;
  padding: 2% 2%;
}

.container-sm {
  margin-top: 5%;
  background-color: #f8f9fa;
  border-radius: 12px 12px;
  padding: 2% 2%;
}

h1 {
  text-align: center;
}

p {
  padding-top: 1%;
  text-align: justify;
}


#btn-caption{
  font-style: italic;
  font-size: 10px;
  float: right;
  margin-right: 1%;
}

</style>