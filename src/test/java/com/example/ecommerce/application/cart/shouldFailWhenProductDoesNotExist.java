@Test
void shouldFailWhenProductDoesNotExist() {
    var productRepository = new InMemoryProductRepository();
    var cartRepository = new InMemoryCartRepository();

    var useCase = new AddItemToCartUseCase(
            productRepository,
            cartRepository
    );

    var userId = UUID.randomUUID();
    var productId = UUID.randomUUID();

    assertThrows(
            IllegalArgumentException.class,
            () -> useCase.execute(userId, productId, 1)
    );
}
