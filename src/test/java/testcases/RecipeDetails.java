package testcases;

public class RecipeDetails {

    // Dietary elimination categories
    public static final String[] EGGETARIAN_ELIMINATE_OPTIONS = {
        "veggie", "without egg", "eggless"
    };

    public static final String[] VEGAN_ELIMINATE_OPTIONS = {
        "egg", "milk", "honey", "butter", "cheese", "ghee", "gelatin",
        "mayonnaise", "cream", "whey", "casein", "paneer"
    };

    /*-----------------------------------------------------------
     * Low Fat Vegan (LFV) diet 
     * 
     * LFV_ELIMINATE includes foods to eliminate.
     *-----------------------------------------------------------*/
    public static final String LFV_ELIMINATE = "pork, meat, poultry, fish, sausage, ham, salami, bacon, milk, cheese, yogurt, butter, ice cream, egg, prawn, " +
            "oil, olive oil, coconut oil, soybean oil, corn oil, safflower oil, sunflower oil, rapeseed oil, peanut oil, cottonseed oil, canola oil, mustard oil, " +
            "cereals, tinned vegetable, bread, maida, atta, sooji, poha, cornflake, cornflour, pasta, white rice, pastry, cakes, biscuit, soy, soy milk, " +
            "white miso paste, soy sauce, soy curls, edamame, soy yogurt, soy nut, tofu, pies, chips, crackers, potato, sugar, jaggery, glucose, fructose, " +
            "corn syrup, cane sugar, aspartame, cane solid, maltose, dextrose, sorbitol, mannitol, xylitol, maltodextrin, molasses, brown rice syrup, splenda, nutra sweet, stevia";
    
    public static final String LFV_ADD = "lettuce, kale, chard, arugula, spinach, cabbage, pumpkin, sweet potatoes, purple potatoes, yams, " +
            "turnip, parsnip, karela, bittergourd, beet, carrot, cucumber, red onion, white onion, broccoli, cauliflower, celery, artichoke, bell pepper, " +
            "mushroom, tomato, sweet and hot pepper, banana, mango, papaya, plantain, apple, orange, pineapple, pear, tangerine, all berry varieties, " +
            "all melon varieties, peach, plum, nectarine, avocado, amaranth, rajgira, ramdana barnyard, buckwheat, ragi, millet, lentil, pulse, moong dhal, " +
            "masoor dhal, toor dhal, urd dhal, lobia, rajma, matar, almond, cashew, pistachio, brazil nut, walnut, pine nut, hazelnut, macadamia nut, pecan, peanut";
    
  //To add if not fully Vegan
    public static final String LFV_TO_ADD = "Butter, ghee, salmon, mackerel , sardines";
    
    /*-----------------------------------------------------------
     * Low-Carb High-Fat (LCHF) diet 
     *-----------------------------------------------------------*/
    public static final String LCHF_ELIMINATE = "ham, sausage, tinned fish, tuna, sardines, yams, beets, parsnip, turnip, rutabagas, carrot, yuca, " +
            "celery root, horseradish, daikon, jicama, radish, pumpkin, squash, whole fat milk, low fat milk, fat free milk, evaporated milk, condensed milk, " +
            "curd, buttermilk, ice cream, flavored milk, sweetened yogurt, soft cheese, grain, wheat, oats, barley, rice, millet, jowar, bajra, corn, dal, lentil, " +
            "banana, mango, papaya, plantain, apple, orange, pineapple, pear, tangerine, all melon varieties, peach, plum, nectarine, olive oil, coconut oil, " +
            "soybean oil, corn oil, safflower oil, sunflower oil, rapeseed oil, peanut oil, cottonseed oil, canola oil, mustard oil, sugar, jaggery, glucose";
    
    public static final String LCHF_ADD = "fish, prawn, poultry, egg, onion, garlic, turmeric, ginger, butter, ghee, hard cheese, paneer, cottage cheese, " +
            "sour cream, greek yogurt, hung curd, almond, pistachio, brazil nut, walnut, pine nut, hazelnut, macadamia nut, pecan, sunflower seed, sesame seed, " +
            "chia seed, flax seed, blueberry, blackberry, strawberry";
    
    
    // Allergy-related filters 
    public static final String ALLERGY = "Milk, Soy, Egg, Sesame, Peanuts, walnut, almond, hazelnut, pecan, cashew, pistachio, Shell fish, Seafood";
    
    // Recipe categories and cuisine categories 
    public static final String[] RECIPE_CATEGORY_OPTIONS = {"Breakfast", "Lunch", "Snack", "Dinner", "Drink"};
    public static final String CUISINE_CATEGORY = "Indian, South Indian, Rajasthani, Punjabi, Bengali, Orissa, Gujarati, Maharashtrian, Andhra, Kerala, " +
            "Goan, Kashmiri, Himachali, Tamil Nadu, Karnataka, Sindhi, Chhattisgarhi, Madhya Pradesh, Assamese, Manipuri, Tripuri, Sikkimese, Mizo, Arunachali, " +
            "Uttarakhand, Haryanvi, Awadhi, Bihari, Uttar Pradesh, Delhi, North Indian";
    
    // Subcategories based on food types
    public static final String[] SUBCATEGORY = {"Roti", "Soup", "Salad", "Starter", "Rice", "Noodles", "Curry"};
    
    // Food processing methods (for LFV diet)
    public static final String[] FOOD_PROCESSING = {"Raw", "Steamed", "Boiled", "Porched", "Sauted", "Airfryed", "Pan fried"};
    
      
    // Food processing methods to avoid (as per both Excel files for LFV elimination)
    public static final String[] LFV_RECIPE_TO_AVOID = {"fried food", "microwave meals", "ready meals", "chips", "crackers"};
    
    // Optional recipes for LFV diet
    public static final String LFV_OPTIONAL_RECIPES = "Herbal drinks, Coffee, Tea";
    public static final String LFV_OPTIONAL_RECIPES_TO_AVOID = "Milk, Sugar";
    public static final String LFV_OPTIONAL_RECIPES_TO_ELIMINATE = "Snack, Snacks";
}