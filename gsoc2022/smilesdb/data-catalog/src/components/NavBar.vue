<template>
  <nav class="navbar navbar-expand-lg bg-dark">
    <div class="container-fluid">
      <a class="navbar-brand">
        <router-link to="/">SEAGrid Home</router-link>
      </a>
      <button aria-controls="navbarScroll" aria-expanded="false" aria-label="Toggle navigation" class="navbar-toggler"
              data-bs-target="#navbarScroll" data-bs-toggle="collapse" type="button">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div id="navbarScroll" class="collapse navbar-collapse">
        <ul class="navbar-nav me-auto my-2 my-lg-0 navbar-nav-scroll" style="--bs-scroll-height: 100px;">
        </ul>
        <!--        <form class="d-flex" role="search">-->
        <!--          <button class="btn btn-outline-primary" type="button" v-on:click="loadAuthURL">Search</button>-->
        <!--        </form>-->
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  name: "NavBar",
}
</script>

<style scoped>

a{
  padding-left: 18%;
  color: #f8f9fa;
}
a:hover{
  color: #007bff;
  text-decoration: none;
}

a:active{
  font-weight: bold;
}
</style>