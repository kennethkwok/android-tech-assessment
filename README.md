# Pelago Android Challenge

This is a project for the Pelago Android coding challenge, based around the following tech stack:

- Kotlin
- MVVM
- Jetpack Compose
- Coroutines
- Hilt
- Retrofit

## Summary of architecture
This app has been built with Compose and MVVM. The ViewModel communicates with the repository 
layer which fetches facts from the API using Retrofit. Facts which are displayed to the user are 
persisted (based on ACs defined below) in local storage using the Room library. There is no real 
navigation to other screens required in the app, but should that be needed in the future it can 
easily be added to AppNavGraph file (which uses Compose NavHost).

## Testing
Unit tests and Instrumented test are included. Detekt is also included for catching code smells .

## Additional notes
Some basic form of error handling has also been built despite it not being part of the ACs. This 
covers the basic scenarios where the API cannot be reached / network error occurs / user has 
airplane mode switched on.

A data class has intentionally been chosen to represent the UI state rather than a sealed class. 
This allows for greater flexibility in dealing with various combinations of states which can occur.

## Challenge

Given the acceptance criteria below, you should identify the missing parts of the code and implement them.

While the feature itself is pretty simple, we encourage you to be creative and demonstrate your expertise when it comes
to architecture, user experience, and overall best practices.
We understand working on such a task can be pretty time consuming so your solution doesn't have to be perfect, but it
should be good enough to give us an idea of your skills and how you approach a task.

## Acceptance criteria

### Scenario 1

**Given** I am on the home screen \
**When** The screen is first loaded \
**Then** A random fact is displayed

### Scenario 2

**Given** I am on the home screen \
**When** I press on the "More facts!" button \
**Then** A new random fact is displayed

### Scenario 3

**Given** I am on the home screen \
**When** I press on the "More facts!" button \
**Then** The previously displayed fact gets added to a list* \
**And** The list should be limited to the last 3 facts (with newer facts replacing older ones)

*The list should be displayed below the "More facts!" button

### Scenario 4

**Given** I am on the home screen \
**When** I swipe (left or right) on a list item \
**Then** That fact is removed from the list

## Resources

- [Random facts API Documentation](https://uselessfacts.jsph.pl/)

## Submission

You can either send us a link to a public repository (GitHub, Bitbucket, etc.) or a zip file with your project when
you're done.