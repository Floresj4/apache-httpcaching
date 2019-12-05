# Apache-httpcaching

Exploring the httpclient-cache add-on provided by Apache HTTP Components.

## Setup

This repository is setup as Maven module consisting of two projects - the service and a client.

### httpcaching-service

A simple REST application using SpringBoot to serve a single resource at /get/some/resource.  The resource returned by this service is a small jpeg file with Cache-Control headers set to public and a max-age of 10 minutes.

### httpcaching-client

A client application using Apache httpclient and httpclient-cache addon designed to request the /get/some/resource multiple times in order to observe the initial cache miss and subsequent cache hits. 