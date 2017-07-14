describe("A suite", function() {
    it("contains spec with an expectation", function() {
        expect(true).toBe(true);
    });
});


describe("A service", function() {
    var service;

    beforeEach(module('powerUp'));
    beforeEach(inject(function (PaginationService) {
        service = PaginationService;
    }));

    describe('Constructor', function () {

        it('assigns a name', function () {
            debugger;
            expect(service).to.have.property('get');
        });

    });
});