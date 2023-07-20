package com.eriksandoval.repositorybasedmongodbservicedemoproject;

class PlaneFinderPoller(private val repository: AircraftRepository) {
    
    private val client = WebClient.create("http://localhost:7634/aircraft")

    @Scheduled(fixedRate = 1000)
    private fun pollPlanes() {
        repository.deleteAll()

        client.get()
            .retrieve()
            .bodyToFlux<Aircraft>()
            .filter { !it.reg.isNullOrEmpty() }
            .toStream()
            .forEach { repository.save(it) }

        println("Polling completed - List of planes:")
        repository.findAll().forEach { println(it) }
        

    }
}