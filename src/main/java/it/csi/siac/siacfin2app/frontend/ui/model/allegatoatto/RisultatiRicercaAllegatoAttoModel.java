/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccecser.frontend.webservice.msg.InviaAllegatoAtto;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ControlloImportiImpegniVincolati;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;

/**
 * Model per la visualizzazione dei risultati di ricerca per l'Allegato Atto.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 15/set/2014
 * 
 */
public class RisultatiRicercaAllegatoAttoModel extends RisultatiRicercaAllegatoAttoBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 7166471395331168109L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaAllegatoAttoModel() {
		super();
		setTitolo("Risultati di ricerca allegati atto");
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link AnnullaAllegatoAtto}.
	 * 
	 * @return la request creata
	 */
	public AnnullaAllegatoAtto creaRequestAnnullaAllegatoAtto() {
		AnnullaAllegatoAtto request = creaRequest(AnnullaAllegatoAtto.class);
		
		// Creo l'atto per l'annullamento a partire dall'uid
		AllegatoAtto aa = new AllegatoAtto();
		aa.setUid(getUidAllegatoAtto());
		aa.setEnte(getEnte());
		request.setAllegatoAtto(aa);
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link CompletaAllegatoAtto}.
	 * 
	 * @return la request creata
	 */
	public CompletaAllegatoAtto creaRequestCompletaAllegatoAtto() {
		CompletaAllegatoAtto request = creaRequest(CompletaAllegatoAtto.class);
		
		// Creo l'atto per il completamento a partire dall'uid
		AllegatoAtto aa = new AllegatoAtto();
		aa.setUid(getUidAllegatoAtto());
		aa.setEnte(getEnte());
		request.setAllegatoAtto(aa);
		
		request.setBilancio(getBilancio());
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link StampaAllegatoAtto}.
	 * 
	 * @return la request creata
	 */
	public StampaAllegatoAtto creaRequestStampaAllegatoAtto() {
		StampaAllegatoAtto request = creaRequest(StampaAllegatoAtto.class);
		
		request.setEnte(getEnte());
		
		AllegatoAtto aa = new AllegatoAtto();
		aa.setUid(getUidAllegatoAtto());
		aa.setEnte(getEnte());
		
		request.setAllegatoAtto(aa);
		request.setBilancio(getBilancio());
		request.setAnnoEsercizio(getAnnoEsercizioInt()!=null?getAnnoEsercizioInt():(getBilancio()!=null&&getBilancio().getAnno()!=0?getBilancio().getAnno():null));
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link InviaAllegatoAtto}.
	 * 
	 * @return la request creata
	 */
	public InviaAllegatoAtto creaRequestInviaAllegatoAtto() {
		InviaAllegatoAtto request = creaRequest(InviaAllegatoAtto.class);
		
		AllegatoAtto aa = new AllegatoAtto();
		aa.setUid(getUidAllegatoAtto());
		aa.setEnte(getEnte());
		request.setAllegatoAtto(aa);
		
		request.setBilancio(getBilancio());
		request.setAnnoEsercizio(getAnnoEsercizioInt()!=null?getAnnoEsercizioInt():(getBilancio()!=null&&getBilancio().getAnno()!=0?getBilancio().getAnno():null));
		
		
		return request;
	}

	/**
	 * 
	 * @return ControlloImportiImpegniVincolati
	 */
	//SIAC-6688
	public ControlloImportiImpegniVincolati creaRequestControlloImportiImpegniVincolati() {
		ControlloImportiImpegniVincolati request = creaRequest(ControlloImportiImpegniVincolati.class);
		List<Integer> listaAllegatoAttoId = new ArrayList<Integer>();
		listaAllegatoAttoId.add(getUidAllegatoAtto());
		request.setListaAllegatoAttoId(listaAllegatoAttoId );		
		return request;
	}

}
