<?php

function getExecutablePath(){
   return "/home/abhishek/Desktop/GenApp/abhishektest/bin";
}


function getModulesNames(){
	$modules = array();
	array_push($modules,"center");
	array_push($modules,"align");
	array_push($modules,"filetest");
	array_push($modules,"data_interpolation");
	array_push($modules,"test");
	array_push($modules,"build_1");
	array_push($modules,"build_2");
	array_push($modules,"interact_1");
	array_push($modules,"interact_2");
	array_push($modules,"simulate_1");
	array_push($modules,"simulate_2");
	array_push($modules,"calculate_1");
	array_push($modules,"calculate_2");
	array_push($modules,"analyze_1");
	array_push($modules,"analyze_2");
	
	return $modules;
}
?>