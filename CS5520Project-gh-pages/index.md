---
layout: default
title: Jianqing Ma, Horrible Cop
---

## Posts

<ul class="posts">

	  {% for post in site.posts %}
	    <li><span>{{ post.date | date_to_string }}</span> » <a href="{{ post.url | relative_url }}" title="{{ post.title }}">{{ post.title }}</a></li>
	  {% endfor %}
	</ul> 
