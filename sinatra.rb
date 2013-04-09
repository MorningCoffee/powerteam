# sinatra.rb
require 'sinatra'
require 'net/http'
require 'uri'
require 'rubygems'
require "json"
require "haml"

get '/discovery/:discovery_id' do

  #{params[:discovery_id]}
  json_content = Net::HTTP.get(URI.parse("http://192.168.1.2:8090/discovery/discoveries/"))
  puts json_content
  
  json_parsed = JSON.parse(json_content)
  
  page = File.read("discovery.html")
  
  begin
    page.sub! "<!--@name-->", json_parsed[0]['name'].to_s
    page.sub! "<!--@state-->", json_parsed[0]['state'].to_s
    page.sub! "<!--@complete-->", json_parsed[0]['percent-complete'].to_s
    page.sub! "<!--@time-->", json_parsed[0]['elapsed-time'].to_s
  rescue NoMethodError
    page.sub! "<!--@mess-->", "Threre is no such discovery"
  end
  
  haml page
end