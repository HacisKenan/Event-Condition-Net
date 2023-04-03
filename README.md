<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a name="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->



<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Issues][issues-shield]][issues-url]
[![LinkedIn][linkedin-shield]][linkedin-url]

<!-- PROJECT LOGO -->
<br />
  <p align="center">
    <br />
    <a href="https://github.com/HacisKenan/Event-Condition-Net"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/HacisKenan/Event-Condition-Net/issues">Report Bug</a>
    ·
    <a href="https://github.com/HacisKenan/Event-Condition-Net/issues">Request Feature</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
      </ul>
    </li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

Following repository is my second hobby project and is about creating a datastructure to store a Event-Condition-Net, to simulate it and find out possible traces. This should be visualized with a gui created in java swing. In the future a web page is a possibility.

Explanation Event-Condition Nets
* Event Condition Nets (ECN) are a type of flowchart used to describe how a system behaves under different conditions. They consist of events, conditions, tokens and places.
* An event is something that triggers the system to perform an action. After triggering, the tokens from the places before the event get moved to the places after the event
* A condition is a requirement that must be met for the system to take a particular action. A condition is met, when all of the places before the event hold a token
* An place is a kind of vertice which is related to the event and is "acitivated" when it holds a token

Simulation
* tokens are represented by threads that check if a event is ready to fire
* if an event is not ready to fire the thread waits until notified
* if an event is ready to fire the thread fires the event and places an token in the places after the event
* for every token it places it starts a new thread with the places after the event
* the thread then notifies all other threads

Current project status
* created class to store a Event-Condition-Net
* programmed program to simulate Event-Condition-Net
* programmed simple gui in java swing, where you can input your Event-Condition-Net in set notation and get an output with possible traces
* visualisation of petri net with mxGraph (https://github.com/jgraph/mxgraph/tree/master/java)
* current project version assumes that the first and only start token to be placed is the first place that is being inputted

Future features
* improved simulation, with multiplite start tokens
* more analyzing possibilities
* webpage
* .exe file

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- GETTING STARTED -->
## Getting Started

1. Clone repository in preferred IDE and use the file test.java in "main" module and "gui" package to run the program. 
2. Run the program -> a gui should popup with already filled out input for testing
<img width="450" alt="image" src="https://user-images.githubusercontent.com/71691437/229566780-d46e99e8-20e7-4383-8616-ddab536c2c39.png">
3. input your places in following notation 
<img width="202" alt="image" src="https://user-images.githubusercontent.com/71691437/229567207-084e3fc9-6ff4-49e1-b254-470952351784.png">
4. input your events in following notation
<img width="200" alt="image" src="https://user-images.githubusercontent.com/71691437/229567258-b3a96ede-162e-4c74-b415-9d53d6098b93.png">
5. input your edges in following notation
<img width="200" alt="image" src="https://user-images.githubusercontent.com/71691437/229567344-20943e2f-d81d-47e3-938c-51a286e48984.png">
6. press "start simulation" and your output should look like this
<img width="549" alt="image" src="https://user-images.githubusercontent.com/71691437/229567554-baa5cc88-1cb6-493b-b2f2-a0650d380c80.png">
<img width="157" alt="image" src="https://user-images.githubusercontent.com/71691437/229567461-68466007-4be8-48dc-b4f3-95c7de867a0a.png">
This output shows that based on your input there are two possible traces, meaning that either your event T2 or T3 fires first.

### Prerequisites
There are no prerequisites needed.

### Usages
Following repository has been used to visualize EC-Net in Java Swing (all credits to the creator): https://github.com/jgraph/mxgraph/tree/master/java

<!-- ROADMAP -->
## Roadmap

- [x] Add ECN class
- [x] Add possibility to simulate ECN with one start token
- [x] Add simple gui to input ECN in set notation and find out what possible traces are possible
- [ ] Add possibility to simulate ECN with multiple start tokens
- [ ] Add analysis tool to check if wanted traces and possible traces allign correctly
- [ ] Add analysis to check for deadlocks in ECN
- [X] Add automatic visulisation of ECN
- [ ] Add .exe file
- [ ] host webpage


<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTACT -->
## Contact

Hacisalihoglu Kenan - kenanhacisalihoglu@gmx.at

Project Link: [https://github.com/HacisKenan/Graph-Datastructure-and-Algorithms](https://github.com/HacisKenan/Graph-Datastructure-and-Algorithms)

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/othneildrew/Best-README-Template.svg?style=for-the-badge
[contributors-url]: https://github.com/othneildrew/Best-README-Template/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/othneildrew/Best-README-Template.svg?style=for-the-badge
[forks-url]: https://github.com/othneildrew/Best-README-Template/network/members
[stars-shield]: https://img.shields.io/github/stars/othneildrew/Best-README-Template.svg?style=for-the-badge
[stars-url]: https://github.com/HacisKenan/Graph-Datastructure-and-Algorithms/stargazers
[issues-shield]: https://img.shields.io/github/issues/othneildrew/Best-README-Template.svg?style=for-the-badge
[issues-url]: https://github.com/HacisKenan/Event-Condition-Net/issues
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/kenan-hacisalihoglu/
[product-screenshot]: images/screenshot.png
[Next.js]: https://img.shields.io/badge/next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white
[Next-url]: https://nextjs.org/
[React.js]: https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB
[React-url]: https://reactjs.org/
[Vue.js]: https://img.shields.io/badge/Vue.js-35495E?style=for-the-badge&logo=vuedotjs&logoColor=4FC08D
[Vue-url]: https://vuejs.org/
[Angular.io]: https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white
[Angular-url]: https://angular.io/
[Svelte.dev]: https://img.shields.io/badge/Svelte-4A4A55?style=for-the-badge&logo=svelte&logoColor=FF3E00
[Svelte-url]: https://svelte.dev/
[Laravel.com]: https://img.shields.io/badge/Laravel-FF2D20?style=for-the-badge&logo=laravel&logoColor=white
[Laravel-url]: https://laravel.com
[Bootstrap.com]: https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white
[Bootstrap-url]: https://getbootstrap.com
[JQuery.com]: https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jquery&logoColor=white
[JQuery-url]: https://jquery.com 

