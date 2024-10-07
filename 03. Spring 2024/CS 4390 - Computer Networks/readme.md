<h1>CS 4390 - Computer Networks</h1>

<h2>Course Description</h2>
<p>The design and analysis of computer networks. Topics include: the ISO reference model, transmission media, medium-access protocols, LANs, data link protocols, routing, congestion control, internetworking, and connection management.</p>
<p>I.e., by the time you get out of this course you should basically know how data is transferred across a network.</p>

<h2>Project</h2>
<p>We will simulate an Internetwork by having a group of Unix processes running in the background (batch mode basically). Each process will represent a node in the network: a bridge, a router, or a host PC. Since these are processes and not actual computer equipment, we will use text files (good 'ol Unix txt files) to represent physical links. I.e., when a node transmits a message, what it really is doing is appending the message to a text file that represents the physical link (more on this later). The reason I use text files and not sockets is twofold: a) I don't require you to know sockets in the class, and b) at the end of the execution, each link (i.e. text file) will have stored in it all the messages that were sent between the two nodes at its endpoints. This actually makes our grading easier.</p>

<h2>Documents</h2>
<li>ARP</li>
<li>Bridge</li>
<li>Broadcast</li>
<li>Ethernet</li>
<li>Host</li>
<li>IP</li>
<li>Router</li>
<li>Transport</li>
<li>README.txt</li>
