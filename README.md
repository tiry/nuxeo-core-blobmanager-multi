# Nuxeo BlobManager Multi

## Goal

In most of the cases, we want the pre-production to have the same data than the production: this makes sense. However, this comes at a cost:

 - we store the data twice
 - we pay for transferring data between prod and preprod

This cost does not seem that much of an issue for the Database, but for the binaries, this is interesting to find a better solution:

 - S3 cost becomes an issue when the size grows
 - replicating a bucket takes time and is expensive

The idea is to implement a "smart" S3BinaryManager that is dedicated to pre-production use case:

 - Reads data from production bucket (and fall back on the local bucket)
 - Write data to a local bucket

<img src="https://www.lucidchart.com/publicSegments/view/bda384f8-15c2-482d-83ed-36feae3738e3/image.png" width="800px"/>

This way:

 - we never modify data in production
 - we have access to all binaries from production without paying the storage of the replication

## Principles

We can not use directly the `BlobDispatcher` approach since we want to do the routing based on Read vs Write operation (and not the Document).

Ideally, we want to avoid falling back on a custom `BinaryManager` implementation: so that we do not depend on any specific storage implemenation (i.e. AWS S3).

So, the starting point is a custom `BlobProvider` that does *"like the BlobDispatcher"* but for binaries based on Read vs Write operations.

    readBlob  -> try {preprod.readBlob} catch {prod.readBlob}
    writeBlob -> preprod.writeBlob

Open Question: we would like to prevent any GC from the preprod side, but since the GC is implemented at the `BinaryManager` level, not sure how to prevent it without using a custom BinaryManager or adding a configuration flag in the `DefaultBinaryGarbageCollector`.

## Building

    # mvn clean install

## How to deploy 



## Limitations / TODO

 - rename maven modules from `binarymanager` to `blobmanager` ... pff
 - handle GC

## About Nuxeo

Nuxeo dramatically improves how content-based applications are built, managed and deployed, making customers more agile, innovative and successful. Nuxeo provides a next generation, enterprise ready platform for building traditional and cutting-edge content oriented applications. Combining a powerful application development environment with SaaS-based tools and a modular architecture, the Nuxeo Platform and Products provide clear business value to some of the most recognizable brands including Verizon, Electronic Arts, Netflix, Sharp, FICO, the U.S. Navy, and Boeing. Nuxeo is headquartered in New York and Paris. More information is available at www.nuxeo.com.
