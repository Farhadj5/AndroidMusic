#!/usr/bin/perl
#
# ./rip index.html
# This script will rip songs off of 3lau.com


while(<>){
if($_ =~ m/.*https:\/\/s3-us-west-2.amazonaws.com\/3lau\/3lau-haus/){
@a = split(/"/,$_);
`wget @a[1]`;

}
}
