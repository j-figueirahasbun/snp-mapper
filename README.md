# SNPMapper

## Status

![example workflow](https://github.com/j-figueirahasbun/snp-mapper/.github/workflows/github-ci.yml/badge.svg)

## Goal

Create an app where single nucleotide polymorphism (SNP) (genetic mutations) are linked with genes through an amalgamation of different types of mappings.

## Set-Up Guide

To back-end uses SpringBoot framework. To start it, we can use the following:

```
./gradle bootRun
```
The front-end is an Angular project. Before starting it, install the dependencies:

```
cd client
npm install
```

After that, we can use start the front-end server using the following:

```
npm start
```

If you start the front-end server, you will see that it listens at http://localhost:4200.
Navigate to this url in your browser.



## MoSCoW

**Musts:**
- Input of genetic variant identifier provides positional mapping to a gene
- Input of genetic variant identifier provides functional mapping to a gene
- Input of genetic variant identifier provides eQTL mapping to a gene
- Combination of different mappings provide the most likely gene

**Should:**
- Mapped gene extra information is provided (function, related diseases)
- Other genes mapped by different tools are also provided as other potential candidates
- Display rank score per gene

**Could:**
- Can input genetic mutation by position
- Implicated pathways are provided
- Provide login feature for saving previous mappings

**Won't:**

- 


## Personal Goals

**Seek Help Promptly**

Ask for assistance as soon as I encounter an issue or challenge.

**Stay Organized**

Organize issue board more efficiently and keep up to track with weekly goals.


## Technical Goals

**_Primary Goals:_**

**Kotlin:**
Practice with the use of Kotlin as a programming language.

**Angular:**
Use Angular DB for the front-end to learn and practice a new framework.



**_Secondary Goals:_**

**AWS cloud:**
If Possible, integrate an AWS cloud service in the project to further practice previously studied principles.

**IntelliJ:** Use a different IDE than the usual VSCode

## Authors and acknowledgment
[Show your appreciation to those who have contributed to the project.]

## Project status
[If you have run out of energy or time for your project, put a note at the top of the README saying that development has slowed down or stopped completely. Someone may choose to fork your project or volunteer to step in as a maintainer or owner, allowing your project to keep going. You can also make an explicit request for maintainers.]
