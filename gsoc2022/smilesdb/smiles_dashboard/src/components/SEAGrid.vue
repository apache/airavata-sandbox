<template>
  <div class="container">
    <h1>Welcome to SEAGrid Data Catalog</h1>
    <p>SEAGrid Data Catalog provides a sleek web interface for you to browse and
      search through your SEAGrid data. Currently the system can index outputs of
      several computational chemistry applications including Gaussian, Gamess,
      Molpro and NWChem. Also it allows to publish your data into research data
      publishing systems, do browser based visualization of molecular structure
      and properties and to run complex search queries to filter the data.
      So now you don't need to download all the data into your local machine after
      running a HPC application but select only the interesting data based on the
      results of configured post processing steps in the system.</p>
    <img class="sys-architect" src="../assets/architect.png" alt="SEAGrid Architecture" />
  </div>
</template>

<script>
export default {
  name: "SEAGrid"
}
</script>

<style scoped>

  .container{
    width: 70em;
    height: 80em;
    margin-left: auto;
    margin-right: auto;
  }
  h1 {
    padding-top: 1em;
    position: center;
    text-decoration: black;
  }
  p{
    display: block;
    margin-block-start: 1em;
    margin-block-end: 1em;
    margin-inline-start: 0px;
    margin-inline-end: 0px;
    text-align: justify ;
    font-size: 21px;
  }
  .sys-architect{
    height: 500px;
    width: 700px;
  }
</style>