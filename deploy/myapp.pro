-injars vocabulary-cards.jar
-libraryjars lib/jsr305-2.0.1.jar

-libraryjars checkthread-annotations-1.0.9.jar
-libraryjars commons-lang3-3.1.jar
-libraryjars gson-2.2.3.jar
-libraryjars guava-14.0.1.jar
-libraryjars hamcrest-core-1.3.jar
-libraryjars javatuples-1.2.jar
-libraryjars jcip-annotations.jar
-libraryjars jetty-all-7.0.2.v20100331.jar
-libraryjars junit-4.11.jar
-libraryjars mockito-all-1.9.5.jar
-libraryjars servlet-api-2.5.jar
-libraryjars snakeyaml-1.12.jar

-outjars vocabulary-cards-dist.jar

-dontoptimize
-dontobfuscate
-dontwarn sun.misc.Unsafe
-dontwarn com.google.common.collect.MinMaxPriorityQueue

-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}