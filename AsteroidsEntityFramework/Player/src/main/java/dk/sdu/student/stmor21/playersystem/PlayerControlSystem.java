package dk.sdu.student.stmor21.playersystem;

import dk.sdu.student.stmor21.common.data.Entity;
import dk.sdu.student.stmor21.common.data.GameData;
import dk.sdu.student.stmor21.common.data.GameKeys;
import dk.sdu.student.stmor21.common.data.World;
import dk.sdu.student.stmor21.common.data.entityparts.LifePart;
import dk.sdu.student.stmor21.common.data.entityparts.MovingPart;
import dk.sdu.student.stmor21.common.data.entityparts.PositionPart;
import dk.sdu.student.stmor21.common.services.IEntityProcessingService;
import dk.sdu.student.stmor21.common.util.SPILocator;
import dk.sdu.student.stmor21.commonBullet.BulletSPI;
import dk.sdu.student.stmor21.commonPlayer.Player;

import static dk.sdu.student.stmor21.common.data.GameKeys.*;


public class PlayerControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity player : world.getEntities(Player.class)) {
            PositionPart positionPart = player.getPart(PositionPart.class);
            MovingPart movingPart = player.getPart(MovingPart.class);
            LifePart lifePart = player.getPart(LifePart.class);

            movingPart.setLeft(gameData.getKeys().isDown(LEFT));
            movingPart.setRight(gameData.getKeys().isDown(RIGHT));
            movingPart.setUp(gameData.getKeys().isDown(UP));

            if(gameData.getKeys().isDown(GameKeys.SPACE)){
                for (BulletSPI bullet : SPILocator.locateAll(BulletSPI.class)) {
                    world.addEntity(bullet.createBullet(player, gameData));
                }

            }


            movingPart.process(gameData, player);
            positionPart.process(gameData, player);
            lifePart.process(gameData,player);

            if(lifePart.getLife() <= lifePart.getExpiration()){
                world.removeEntity(player);
            }

            updateShape(player);
        }
    }

    private void updateShape(Entity entity) {
        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        PositionPart positionPart = entity.getPart(PositionPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();
        float radians = positionPart.getRadians();

        shapex[0] = (float) (x + Math.cos(radians) * 8);
        shapey[0] = (float) (y + Math.sin(radians) * 8);

        shapex[1] = (float) (x + Math.cos(radians - 4 * 3.1415f / 5) * 8);
        shapey[1] = (float) (y + Math.sin(radians - 4 * 3.1145f / 5) * 8);

        shapex[2] = (float) (x + Math.cos(radians + 3.1415f) * 3);
        shapey[2] = (float) (y + Math.sin(radians + 3.1415f) * 3);

        shapex[3] = (float) (x + Math.cos(radians + 4 * 3.1415f / 5) * 8);
        shapey[3] = (float) (y + Math.sin(radians + 4 * 3.1415f / 5) * 8);

        entity.setShapeX(shapex);
        entity.setShapeY(shapey);
    }

}
