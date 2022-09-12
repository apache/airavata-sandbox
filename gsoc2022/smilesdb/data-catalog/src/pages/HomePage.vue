<template>
 <div>
   <AppHeader/>
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
   </div>

   <div class="img-container">
     <img src="../assets/data-catalog-overview.png" class="img-fluid" alt="architecture"/>
   </div>

 </div>
</template>

<script>
import AppHeader from "@/components/AppHeader";
export default {
  name: "HomePage",
  components: {AppHeader}
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

.img-container{
  display: block;
  margin-left: auto;
  margin-right: auto;
  margin-top: 3%;
  width: 50%
}

</style>