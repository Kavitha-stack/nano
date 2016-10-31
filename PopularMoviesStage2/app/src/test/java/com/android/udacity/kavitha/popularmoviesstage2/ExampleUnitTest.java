package com.android.udacity.kavitha.popularmoviesstage2;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

      //  Helper h = new Helper();
       // h.connectNetwork(null);

        String ss = "{\"page\":1,\"results\":[{\"poster_path\":\"\\/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg\",\"adult\":false,\"overview\":\"From DC Comics comes the Suicide Squad, an antihero team of incarcerated supervillains who act as deniable assets for the United States government, undertaking high-risk black ops missions in exchange for commuted prison sentences.\",\"release_date\":\"2016-08-03\",\"genre_ids\":[28,80,878],\"id\":297761,\"original_title\":\"Suicide Squad\",\"original_language\":\"en\",\"title\":\"Suicide Squad\",\"backdrop_path\":\"\\/ndlQ2Cuc3cjTL7lTynw6I4boP4S.jpg\",\"popularity\":34.182652,\"vote_count\":306,\"video\":false,\"vote_average\":6.09},{\"poster_path\":\"\\/cGOPbv9wA5gEejkUN892JrveARt.jpg\",\"adult\":false,\"overview\":\"Fearing the actions of a god-like Super Hero left unchecked, Gotham City’s own formidable, forceful vigilante takes on Metropolis’s most revered, modern-day savior, while the world wrestles with what sort of hero it really needs. And with Batman and Superman at war with one another, a new threat quickly arises, putting mankind in greater danger than it’s ever known before.\",\"release_date\":\"2016-03-23\",\"genre_ids\":[28,12,14],\"id\":209112,\"original_title\":\"Batman v Superman: Dawn of Justice\",\"original_language\":\"en\",\"title\":\"Batman v Superman: Dawn of Justice\",\"backdrop_path\":\"\\/vsjBeMPZtyB7yNsYY56XYxifaQZ.jpg\",\"popularity\":32.148365,\"vote_count\":3155,\"video\":false,\"vote_average\":5.51},{\"poster_path\":\"\\/6FxOPJ9Ysilpq0IgkrMJ7PubFhq.jpg\",\"adult\":false,\"overview\":\"Tarzan, having acclimated to life in London, is called back to his former home in the jungle to investigate the activities at a mining encampment.\",\"release_date\":\"2016-06-29\",\"genre_ids\":[28,12],\"id\":258489,\"original_title\":\"The Legend of Tarzan\",\"original_language\":\"en\",\"title\":\"The Legend of Tarzan\",\"backdrop_path\":\"\\/75GFqrnHMKqkcNZ2wWefWXfqtMV.jpg\",\"popularity\":27.910512,\"vote_count\":852,\"video\":false,\"vote_average\":4.77},{\"poster_path\":\"\\/lFSSLTlFozwpaGlO31OoUeirBgQ.jpg\",\"adult\":false,\"overview\":\"The most dangerous former operative of the CIA is drawn out of hiding to uncover hidden truths about his past.\",\"release_date\":\"2016-07-27\",\"genre_ids\":[53,28],\"id\":324668,\"original_title\":\"Jason Bourne\",\"original_language\":\"en\",\"title\":\"Jason Bourne\",\"backdrop_path\":\"\\/AoT2YrJUJlg5vKE3iMOLvHlTd3m.jpg\",\"popularity\":23.715757,\"vote_count\":290,\"video\":false,\"vote_average\":5.11},{\"poster_path\":\"\\/ghL4ub6vwbYShlqCFHpoIRwx2sm.jpg\",\"adult\":false,\"overview\":\"The USS Enterprise crew explores the furthest reaches of uncharted space, where they encounter a mysterious new enemy who puts them and everything the Federation stands for to the test.\",\"release_date\":\"2016-07-07\",\"genre_ids\":[28,12,878],\"id\":188927,\"original_title\":\"Star Trek Beyond\",\"original_language\":\"en\",\"title\":\"Star Trek Beyond\",\"backdrop_path\":\"\\/doqRJwhRFsHHneYG82bM0hSTqpz.jpg\",\"popularity\":21.378374,\"vote_count\":386,\"video\":false,\"vote_average\":6.29},{\"poster_path\":\"\\/kqjL17yufvn9OVLyXYpvtyrFfak.jpg\",\"adult\":false,\"overview\":\"An apocalyptic story set in the furthest reaches of our planet, in a stark desert landscape where humanity is broken, and most everyone is crazed fighting for the necessities of life. Within this world exist two rebels on the run who just might be able to restore order. There's Max, a man of action and a man of few words, who seeks peace of mind following the loss of his wife and child in the aftermath of the chaos. And Furiosa, a woman of action and a woman who believes her path to survival may be achieved if she can make it across the desert back to her childhood homeland.\",\"release_date\":\"2015-05-13\",\"genre_ids\":[28,12,878,53],\"id\":76341,\"original_title\":\"Mad Max: Fury Road\",\"original_language\":\"en\",\"title\":\"Mad Max: Fury Road\",\"backdrop_path\":\"\\/tbhdm8UJAb4ViCTsulYFL3lxMCd.jpg\",\"popularity\":18.391762,\"vote_count\":5061,\"video\":false,\"vote_average\":7.29},{\"poster_path\":\"\\/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg\",\"adult\":false,\"overview\":\"Interstellar chronicles the adventures of a group of explorers who make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage.\",\"release_date\":\"2014-11-05\",\"genre_ids\":[12,18,878],\"id\":157336,\"original_title\":\"Interstellar\",\"original_language\":\"en\",\"title\":\"Interstellar\",\"backdrop_path\":\"\\/xu9zaAevzQ5nnrsXN6JcahLnG4i.jpg\",\"popularity\":16.743009,\"vote_count\":5444,\"video\":false,\"vote_average\":8.13},{\"poster_path\":\"\\/h28t2JNNGrZx0fIuAw8aHQFhIxR.jpg\",\"adult\":false,\"overview\":\"A recently cheated on married woman falls for a younger man who has moved in next door, but their torrid affair soon takes a dangerous turn.\",\"release_date\":\"2015-01-23\",\"genre_ids\":[53],\"id\":241251,\"original_title\":\"The Boy Next Door\",\"original_language\":\"en\",\"title\":\"The Boy Next Door\",\"backdrop_path\":\"\\/vj4IhmH4HCMZYYjTMiYBybTWR5o.jpg\",\"popularity\":16.19817,\"vote_count\":539,\"video\":false,\"vote_average\":4.36},{\"poster_path\":\"\\/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg\",\"adult\":false,\"overview\":\"Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.\",\"release_date\":\"2015-06-09\",\"genre_ids\":[28,12,878,53],\"id\":135397,\"original_title\":\"Jurassic World\",\"original_language\":\"en\",\"title\":\"Jurassic World\",\"backdrop_path\":\"\\/dkMD5qlogeRMiEixC4YNPUvax2T.jpg\",\"popularity\":16.079174,\"vote_count\":4807,\"video\":false,\"vote_average\":6.61},{\"poster_path\":\"\\/gj282Pniaa78ZJfbaixyLXnXEDI.jpg\",\"adult\":false,\"overview\":\"Katniss Everdeen reluctantly becomes the symbol of a mass rebellion against the autocratic Capitol.\",\"release_date\":\"2014-11-18\",\"genre_ids\":[878,12,53],\"id\":131631,\"original_title\":\"The Hunger Games: Mockingjay - Part 1\",\"original_language\":\"en\",\"title\":\"The Hunger Games: Mockingjay - Part 1\",\"backdrop_path\":\"\\/83nHcz2KcnEpPXY50Ky2VldewJJ.jpg\",\"popularity\":15.91193,\"vote_count\":3083,\"video\":false,\"vote_average\":6.72},{\"poster_path\":\"\\/9KQX22BeFzuNM66pBA6JbiaJ7Mi.jpg\",\"adult\":false,\"overview\":\"We always knew they were coming back. Using recovered alien technology, the nations of Earth have collaborated on an immense defense program to protect the planet. But nothing can prepare us for the aliens’ advanced and unprecedented force. Only the ingenuity of a few brave men and women can bring our world back from the brink of extinction.\",\"release_date\":\"2016-06-22\",\"genre_ids\":[28,12,878],\"id\":47933,\"original_title\":\"Independence Day: Resurgence\",\"original_language\":\"en\",\"title\":\"Independence Day: Resurgence\",\"backdrop_path\":\"\\/8SqBiesvo1rh9P1hbJTmnVum6jv.jpg\",\"popularity\":14.887304,\"vote_count\":817,\"video\":false,\"vote_average\":4.65},{\"poster_path\":\"\\/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg\",\"adult\":false,\"overview\":\"The year is 2029. John Connor, leader of the resistance continues the war against the machines. At the Los Angeles offensive, John's fears of the unknown future begin to emerge when TECOM spies reveal a new plot by SkyNet that will attack him from both fronts; past and future, and will ultimately change warfare forever.\",\"release_date\":\"2015-06-23\",\"genre_ids\":[878,28,53,12],\"id\":87101,\"original_title\":\"Terminator Genisys\",\"original_language\":\"en\",\"title\":\"Terminator Genisys\",\"backdrop_path\":\"\\/bIlYH4l2AyYvEysmS2AOfjO7Dn8.jpg\",\"popularity\":14.62367,\"vote_count\":2264,\"video\":false,\"vote_average\":5.94},{\"poster_path\":\"\\/5N20rQURev5CNDcMjHVUZhpoCNC.jpg\",\"adult\":false,\"overview\":\"Following the events of Age of Ultron, the collective governments of the world pass an act designed to regulate all superhuman activity. This polarizes opinion amongst the Avengers, causing two factions to side with Iron Man or Captain America, which causes an epic battle between former allies.\",\"release_date\":\"2016-04-27\",\"genre_ids\":[28,53,878],\"id\":271110,\"original_title\":\"Captain America: Civil War\",\"original_language\":\"en\",\"title\":\"Captain America: Civil War\",\"backdrop_path\":\"\\/m5O3SZvQ6EgD5XXXLPIP1wLppeW.jpg\",\"popularity\":14.607816,\"vote_count\":2406,\"video\":false,\"vote_average\":6.93},{\"poster_path\":\"\\/b77l5vmp6PYsc98LE6Uf1mXtmHh.jpg\",\"adult\":false,\"overview\":\"As two evil sisters prepare to conquer the land, two renegades - Eric the Huntsman - who aided Snow White in defeating Ravenna in Snowwhite and the Huntsman, and his forbidden lover, Sara, set out to stop them.\",\"release_date\":\"2016-04-06\",\"genre_ids\":[28,12,18],\"id\":290595,\"original_title\":\"The Huntsman: Winter's War\",\"original_language\":\"en\",\"title\":\"The Huntsman: Winter's War\",\"backdrop_path\":\"\\/nQ0UvXdxoMZguLuPj0sdV0U36KR.jpg\",\"popularity\":14.285295,\"vote_count\":464,\"video\":false,\"vote_average\":5.67},{\"poster_path\":\"\\/inVq3FRqcYIRl2la8iZikYYxFNR.jpg\",\"adult\":false,\"overview\":\"Based upon Marvel Comics’ most unconventional anti-hero, DEADPOOL tells the origin story of former Special Forces operative turned mercenary Wade Wilson, who after being subjected to a rogue experiment that leaves him with accelerated healing powers, adopts the alter ego Deadpool. Armed with his new abilities and a dark, twisted sense of humor, Deadpool hunts down the man who nearly destroyed his life.\",\"release_date\":\"2016-02-09\",\"genre_ids\":[28,12,35,10749],\"id\":293660,\"original_title\":\"Deadpool\",\"original_language\":\"en\",\"title\":\"Deadpool\",\"backdrop_path\":\"\\/nbIrDhOtUpdD9HKDBRy02a8VhpV.jpg\",\"popularity\":13.782044,\"vote_count\":4579,\"video\":false,\"vote_average\":7.15},{\"poster_path\":\"\\/sM33SANp9z6rXW8Itn7NnG1GOEs.jpg\",\"adult\":false,\"overview\":\"Determined to prove herself, Officer Judy Hopps, the first bunny on Zootopia's police force, jumps at the chance to crack her first case - even if it means partnering with scam-artist fox Nick Wilde to solve the mystery.\",\"release_date\":\"2016-02-11\",\"genre_ids\":[16,12,10751,35],\"id\":269149,\"original_title\":\"Zootopia\",\"original_language\":\"en\",\"title\":\"Zootopia\",\"backdrop_path\":\"\\/mhdeE1yShHTaDbJVdWyTlzFvNkr.jpg\",\"popularity\":13.708456,\"vote_count\":1646,\"video\":false,\"vote_average\":7.4},{\"poster_path\":\"\\/ckrTPz6FZ35L5ybjqvkLWzzSLO7.jpg\",\"adult\":false,\"overview\":\"The peaceful realm of Azeroth stands on the brink of war as its civilization faces a fearsome race of invaders: orc warriors fleeing their dying home to colonize another. As a portal opens to connect the two worlds, one army faces destruction and the other faces extinction. From opposing sides, two heroes are set on a collision course that will decide the fate of their family, their people, and their home.\",\"release_date\":\"2016-05-25\",\"genre_ids\":[28,12,14],\"id\":68735,\"original_title\":\"Warcraft\",\"original_language\":\"en\",\"title\":\"Warcraft\",\"backdrop_path\":\"\\/5SX2rgKXZ7NVmAJR5z5LprqSXKa.jpg\",\"popularity\":12.232168,\"vote_count\":619,\"video\":false,\"vote_average\":6.02},{\"poster_path\":\"\\/dCgm7efXDmiABSdWDHBDBx2jwmn.jpg\",\"adult\":false,\"overview\":\"Deckard Shaw seeks revenge against Dominic Toretto and his family for his comatose brother.\",\"release_date\":\"2015-04-01\",\"genre_ids\":[28,80,53],\"id\":168259,\"original_title\":\"Furious 7\",\"original_language\":\"en\",\"title\":\"Furious 7\",\"backdrop_path\":\"\\/ypyeMfKydpyuuTMdp36rMlkGDUL.jpg\",\"popularity\":12.172003,\"vote_count\":2635,\"video\":false,\"vote_average\":7.42},{\"poster_path\":\"\\/t2mZzQXjpQxmqtJOPpe8Dr2YpMl.jpg\",\"adult\":false,\"overview\":\"An island populated entirely by happy, flightless birds or almost entirely. In this paradise, Red, a bird with a temper problem, speedy Chuck, and the volatile Bomb have always been outsiders. But when the island is visited by mysterious green piggies, it’s up to these unlikely outcasts to figure out what the pigs are up to.\",\"release_date\":\"2016-05-11\",\"genre_ids\":[28,16,35,10751],\"id\":153518,\"original_title\":\"Angry Birds\",\"original_language\":\"en\",\"title\":\"Angry Birds\",\"backdrop_path\":\"\\/3mJcfL2lPfRky16EPi95d2YrKqu.jpg\",\"popularity\":11.821774,\"vote_count\":291,\"video\":false,\"vote_average\":5.8},{\"poster_path\":\"\\/bIXbMvEKhlLnhdXttTf2ZKvLZEP.jpg\",\"adult\":false,\"overview\":\"The quiet life of a terrier named Max is upended when his owner takes in Duke, a stray whom Max instantly dislikes.\",\"release_date\":\"2016-06-24\",\"genre_ids\":[12,16,35,10751],\"id\":328111,\"original_title\":\"The Secret Life of Pets\",\"original_language\":\"en\",\"title\":\"The Secret Life of Pets\",\"backdrop_path\":\"\\/up1Fn4u0EvgeWbQuRDVzOrIy2oP.jpg\",\"popularity\":11.429137,\"vote_count\":436,\"video\":false,\"vote_average\":5.47}],\"total_results\":19544,\"total_pages\":978}";
        JSONObject movieJson = new JSONObject(ss);
       // System.out.println("now : "+movieJson);
        JSONArray movieArray = movieJson.getJSONArray("results");

        System.out.println("before for loop" + movieArray.length());
        assertEquals(4, 2 + 2);
    }
}